/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.plugin.completion.smart

import com.intellij.codeInsight.lookup.LookupElement
import org.jetbrains.jet.lang.types.JetType
import org.jetbrains.jet.lang.types.lang.KotlinBuiltIns
import org.jetbrains.jet.lang.descriptors.ClassDescriptor
import org.jetbrains.jet.renderer.DescriptorRenderer
import com.intellij.codeInsight.completion.InsertHandler
import org.jetbrains.jet.lang.descriptors.Modality
import org.jetbrains.jet.lang.descriptors.ClassKind
import org.jetbrains.jet.plugin.codeInsight.ImplementMethodsHandler
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.codeInsight.completion.InsertionContext
import org.jetbrains.jet.plugin.completion.handlers.KotlinFunctionInsertHandler
import org.jetbrains.jet.plugin.completion.*
import org.jetbrains.jet.plugin.completion.handlers.CaretPosition
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor
import org.jetbrains.jet.lang.descriptors.Visibilities
import org.jetbrains.jet.plugin.util.makeNotNullable
import org.jetbrains.jet.plugin.util.IdeDescriptorRenderers
import org.jetbrains.jet.lang.resolve.BindingContext
import org.jetbrains.jet.lang.descriptors.PackageFragmentDescriptor
import org.jetbrains.jet.lang.resolve.java.descriptor.SamConstructorDescriptor
import org.jetbrains.jet.plugin.caches.resolve.ResolutionFacade
import org.jetbrains.jet.lang.resolve.DescriptorToSourceUtils
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ClassInheritorsSearch
import org.jetbrains.jet.asJava.KotlinLightClass
import org.jetbrains.jet.lang.types.TypeProjection
import org.jetbrains.jet.utils.addIfNotNull
import org.jetbrains.jet.plugin.caches.resolve.JavaResolveExtension
import org.jetbrains.jet.lang.resolve.java.structure.impl.JavaClassImpl
import org.jetbrains.jet.asJava.LightClassUtil
import org.jetbrains.jet.lang.psi.JetClassOrObject
import org.jetbrains.jet.lang.resolve.PossiblyBareType
import org.jetbrains.jet.lang.types.JetTypeImpl
import org.jetbrains.jet.lang.descriptors.annotations.Annotations
import org.jetbrains.jet.lang.resolve.DescriptorUtils
import org.jetbrains.jet.lang.resolve.java.mapping.KotlinToJavaTypesMap
import org.jetbrains.jet.lang.descriptors.ModuleDescriptor
import org.jetbrains.jet.lang.resolve.resolveTopLevelClass
import org.jetbrains.jet.lang.types.TypeProjectionImpl
import org.jetbrains.jet.lang.types.Variance
import org.jetbrains.jet.lang.psi.JetDeclaration

class TypeInstantiationItems(
        val resolutionFacade: ResolutionFacade,
        val moduleDescriptor: ModuleDescriptor,
        val bindingContext: BindingContext,
        val visibilityFilter: (DeclarationDescriptor) -> Boolean,
        val toFromOriginalFileMapper: ToFromOriginalFileMapper,
        val inheritorSearchScope: GlobalSearchScope
) {
    public fun addTo(
            items: MutableCollection<LookupElement>,
            inheritanceSearchers: MutableCollection<InheritanceItemsSearcher>,
            expectedInfos: Collection<ExpectedInfo>
    ) {
        val expectedInfosGrouped: Map<JetType, List<ExpectedInfo>> = expectedInfos.groupBy { it.type.makeNotNullable() }
        for ((type, infos) in expectedInfosGrouped) {
            val tail = mergeTails(infos.map { it.tail })
            addTo(items, inheritanceSearchers, type, tail)
        }
    }

    private fun addTo(
            items: MutableCollection<LookupElement>,
            inheritanceSearchers: MutableCollection<InheritanceItemsSearcher>,
            type: JetType,
            tail: Tail?
    ) {
        if (KotlinBuiltIns.getInstance().isExactFunctionOrExtensionFunctionType(type)) return // do not show "object: ..." for function types

        val classifier = type.getConstructor().getDeclarationDescriptor()
        if (classifier !is ClassDescriptor) return

        addSamConstructorItem(items, classifier, tail)

        val typeArgs = type.getArguments()
        items.addIfNotNull(createTypeInstantiationItem(classifier, typeArgs, tail))

        if (!KotlinBuiltIns.getInstance().isAny(classifier)) { // do not search inheritors of Any
            inheritanceSearchers.addInheritorSearcher(classifier, classifier, typeArgs, tail)

            val javaAnalogFqName = KotlinToJavaTypesMap.getInstance().getKotlinToJavaFqName(DescriptorUtils.getFqNameSafe(classifier))
            if (javaAnalogFqName != null) {
                val javaAnalog = moduleDescriptor.resolveTopLevelClass(javaAnalogFqName)
                if (javaAnalog != null) {
                    inheritanceSearchers.addInheritorSearcher(javaAnalog, classifier, typeArgs, tail)
                }
            }
        }
    }

    private fun MutableCollection<InheritanceItemsSearcher>.addInheritorSearcher(
            descriptor: ClassDescriptor, kotlinClassDescriptor: ClassDescriptor, typeArgs: List<TypeProjection>, tail: Tail?
    ) {
        val _declaration = DescriptorToSourceUtils.descriptorToDeclaration(descriptor) ?: return
        val declaration = if (_declaration.getContainingFile() == toFromOriginalFileMapper.syntheticFile)
            toFromOriginalFileMapper.toOriginalFile(_declaration as JetDeclaration) ?: return
        else
            _declaration

        val psiClass: PsiClass = when (declaration) {
            is PsiClass -> declaration
            is JetClassOrObject -> LightClassUtil.getPsiClass(declaration) ?: return
            else -> return
        }
        add(InheritanceSearcher(psiClass, kotlinClassDescriptor, typeArgs, tail))
    }

    private fun createTypeInstantiationItem(
            classifier: ClassDescriptor,
            typeArgs: List<TypeProjection?>,
            tail: Tail?
    ): LookupElement? {
        var lookupElement = LookupElementFactory.DEFAULT.createLookupElement(classifier, resolutionFacade, bindingContext)

        if (classifier.getKind() == ClassKind.OBJECT) {
            return lookupElement.addTail(tail)
        }

        val isAbstract = classifier.getModality() == Modality.ABSTRACT
        val allConstructors = classifier.getConstructors()
        val visibleConstructors = allConstructors.filter {
            if (isAbstract)
                visibilityFilter(it) || it.getVisibility() == Visibilities.PROTECTED
            else
                visibilityFilter(it)
        }
        if (allConstructors.isNotEmpty() && visibleConstructors.isEmpty()) return null

        var lookupString = lookupElement.getLookupString()
        var allLookupStrings = setOf(lookupString)

        // drop "in" and "out" from type arguments - they cannot be used in constructor call
        val typeArgsToUse = typeArgs.map { if (it != null) TypeProjectionImpl(Variance.INVARIANT, it.getType()) else null }

        var itemText = lookupString + DescriptorRenderer.SHORT_NAMES_IN_TYPES.renderTypeArguments(typeArgsToUse)
        var signatureText: String? = null

        val insertHandler: InsertHandler<LookupElement>
        val typeText = qualifiedNameForSourceCode(classifier) + IdeDescriptorRenderers.SOURCE_CODE.renderTypeArguments(typeArgsToUse)
        if (isAbstract) {
            val constructorParenthesis = if (classifier.getKind() != ClassKind.TRAIT) "()" else ""
            itemText += constructorParenthesis
            itemText = "object: " + itemText + "{...}"
            lookupString = "object"
            allLookupStrings = setOf(lookupString, lookupElement.getLookupString())
            insertHandler = InsertHandler<LookupElement> {(context, item) ->
                val editor = context.getEditor()
                val startOffset = context.getStartOffset()
                val text = "object: $typeText$constructorParenthesis {}"
                editor.getDocument().replaceString(startOffset, context.getTailOffset(), text)
                editor.getCaretModel().moveToOffset(startOffset + text.length - 1)

                shortenReferences(context, startOffset, startOffset + text.length)

                ImplementMethodsHandler().invoke(context.getProject(), editor, context.getFile(), true)
            }
            lookupElement = lookupElement.suppressAutoInsertion()
            lookupElement = lookupElement.assignSmartCompletionPriority(SmartCompletionItemPriority.ANONYMOUS_OBJECT)
        }
        else {
            //TODO: when constructor has one parameter of lambda type with more than one parameter, generate special additional item
            signatureText = when (visibleConstructors.size) {
                0 -> "()"
                1 -> DescriptorRenderer.SHORT_NAMES_IN_TYPES.renderFunctionParameters(visibleConstructors.single())
                else -> "(...)"
            }

            val baseInsertHandler = when (visibleConstructors.size) {
                0 -> KotlinFunctionInsertHandler.NO_PARAMETERS_HANDLER
                1 -> LookupElementFactory.getDefaultInsertHandler(visibleConstructors.single()) as KotlinFunctionInsertHandler
                else -> KotlinFunctionInsertHandler.WITH_PARAMETERS_HANDLER
            }

            insertHandler = object : InsertHandler<LookupElement> {
                override fun handleInsert(context: InsertionContext, item: LookupElement) {
                    context.getDocument().replaceString(context.getStartOffset(), context.getTailOffset(), typeText)
                    context.setTailOffset(context.getStartOffset() + typeText.length)

                    baseInsertHandler.handleInsert(context, item)

                    shortenReferences(context, context.getStartOffset(), context.getTailOffset())
                }
            }
            if (baseInsertHandler.caretPosition == CaretPosition.IN_BRACKETS) {
                lookupElement = lookupElement.keepOldArgumentListOnTab()
            }
            if (baseInsertHandler.lambdaInfo != null) {
                lookupElement.putUserData(KotlinCompletionCharFilter.ACCEPT_OPENING_BRACE, true)
            }
            lookupElement = lookupElement.assignSmartCompletionPriority(SmartCompletionItemPriority.INSTANTIATION)
        }

        //TODO: cannot use lookupElement from context due to KT-6344
        class InstantiationLookupElement(lookupElement: LookupElement) : LookupElementDecorator<LookupElement>(lookupElement) {
            override fun getLookupString() = lookupString

            override fun getAllLookupStrings() = allLookupStrings

            override fun renderElement(presentation: LookupElementPresentation) {
                getDelegate().renderElement(presentation)
                presentation.setItemText(itemText)

                presentation.clearTail()
                if (signatureText != null) {
                    presentation.appendTailText(signatureText!!, false)
                }
                presentation.appendTailText(" (" + DescriptorUtils.getFqName(classifier.getContainingDeclaration()) + ")", true)
            }

            override fun handleInsert(context: InsertionContext) {
                insertHandler.handleInsert(context, getDelegate())
            }

            override fun equals(other: Any?): Boolean {
                if (other === this) return true
                if (other !is InstantiationLookupElement) return false
                if (getLookupString() != other.getLookupString()) return false
                val presentation1 = LookupElementPresentation()
                val presentation2 = LookupElementPresentation()
                renderElement(presentation1)
                other.renderElement(presentation2)
                return presentation1.getItemText() == presentation2.getItemText() && presentation1.getTailText() == presentation2.getTailText()
            }
        }

        return InstantiationLookupElement(lookupElement).addTail(tail)
    }

    private fun addSamConstructorItem(collection: MutableCollection<LookupElement>, `class`: ClassDescriptor, tail: Tail?) {
        if (`class`.getKind() == ClassKind.TRAIT) {
            val container = `class`.getContainingDeclaration()
            val scope = when (container) {
                is PackageFragmentDescriptor -> container.getMemberScope()
                is ClassDescriptor -> container.getStaticScope()
                else -> return
            }
            val samConstructor = scope.getFunctions(`class`.getName())
                                         .filterIsInstance(javaClass<SamConstructorDescriptor>())
                                         .singleOrNull() ?: return
            val lookupElement = LookupElementFactory.DEFAULT.createLookupElement(samConstructor, resolutionFacade, bindingContext)
                    .assignSmartCompletionPriority(SmartCompletionItemPriority.INSTANTIATION)
                    .addTail(tail)
            collection.add(lookupElement)
        }
    }

    private inner class InheritanceSearcher(
            val psiClass: PsiClass,
            val classDescriptor: ClassDescriptor,
            val typeArgs: List<TypeProjection?>,
            val tail: Tail?) : InheritanceItemsSearcher {

        private val typeConstructor = classDescriptor.getTypeConstructor()
        private val baseHasTypeArgs = typeConstructor.getParameters().isNotEmpty()
        private val expectedType = JetTypeImpl(Annotations.EMPTY, typeConstructor, false, typeArgs, classDescriptor.getMemberScope(typeArgs))

        override fun search(nameFilter: (String) -> Boolean, consumer: (LookupElement) -> Unit) {
            val parameters = ClassInheritorsSearch.SearchParameters(psiClass, inheritorSearchScope, true, true, false, nameFilter)
            for (inheritor in ClassInheritorsSearch.search(parameters)) {
                val descriptor = if (inheritor is KotlinLightClass) {
                    val origin = inheritor.origin ?: continue
                    val declaration = if (origin.getContainingFile() == toFromOriginalFileMapper.originalFile)
                        toFromOriginalFileMapper.toSyntheticFile(origin) ?: continue
                    else
                        origin
                    resolutionFacade.resolveToDescriptor(declaration)
                }
                else {
                    resolutionFacade.get(JavaResolveExtension)(inheritor).first.resolveClass(JavaClassImpl(inheritor))
                }  as? ClassDescriptor ?: continue
                if (!visibilityFilter(descriptor)) continue

                val hasTypeArgs = descriptor.getTypeConstructor().getParameters().isNotEmpty()
                val resultingType = if (hasTypeArgs) {
                    val reconstructionResult = PossiblyBareType.bare(descriptor.getTypeConstructor(), false).reconstruct(expectedType)
                    reconstructionResult.getResultingType() ?: continue
                }
                else {
                    descriptor.getDefaultType()
                }
                // check if derived type matches type arguments for base
                if (baseHasTypeArgs && !resultingType.isSubtypeOf(expectedType)) continue

                val lookupElement = createTypeInstantiationItem(descriptor, resultingType.getArguments(), tail) ?: continue
                consumer(lookupElement.assignSmartCompletionPriority(SmartCompletionItemPriority.INHERITOR_INSTANTIATION))
            }
        }
    }
}
