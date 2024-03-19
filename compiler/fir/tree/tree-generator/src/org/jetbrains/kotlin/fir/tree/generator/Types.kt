/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.tree.generator

import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.KtSourceFile
import org.jetbrains.kotlin.KtSourceFileLinesMapping
import org.jetbrains.kotlin.contracts.description.EventOccurrencesRange
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibility
import org.jetbrains.kotlin.descriptors.annotations.AnnotationUseSiteTarget
import org.jetbrains.kotlin.fir.tree.generator.context.generatedType
import org.jetbrains.kotlin.fir.tree.generator.context.type
import org.jetbrains.kotlin.fir.types.ConeClassLikeType
import org.jetbrains.kotlin.fir.types.ConeErrorType
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.ConeSimpleKotlinType
import org.jetbrains.kotlin.generators.tree.TypeKind
import org.jetbrains.kotlin.generators.tree.TypeRef
import org.jetbrains.kotlin.generators.tree.withArgs
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.StandardClassIds
import org.jetbrains.kotlin.types.SmartcastStability
import org.jetbrains.kotlin.types.Variance

val sourceElementType = type<KtSourceElement>()
val sourceFileType = type<KtSourceFile>()
val sourceFileLinesMappingType = type<KtSourceFileLinesMapping>()
val jumpTargetType = type("fir", "FirTarget")
val constKindType = type("types", "ConstantValueKind")
val operationType = type("fir.expressions", "FirOperation")
val classKindType = type<ClassKind>()
val eventOccurrencesRangeType = type<EventOccurrencesRange>()
val inlineStatusType = type("fir.declarations", "InlineStatus")
val varianceType = type<Variance>()
val nameType = type<Name>()
val visibilityType = type<Visibility>()
val effectiveVisibilityType = type("descriptors", "EffectiveVisibility")
val modalityType = type<Modality>()
val smartcastStabilityType = type<SmartcastStability>()
val fqNameType = type<FqName>()
val classIdType = type<ClassId>()
val annotationUseSiteTargetType = type<AnnotationUseSiteTarget>()
val operationKindType = type("contracts.description", "LogicOperationKind")
val coneKotlinTypeType = type<ConeKotlinType>()
val coneErrorTypeType = type<ConeErrorType>()
val coneSimpleKotlinTypeType = type<ConeSimpleKotlinType>()
val coneClassLikeTypeType = type<ConeClassLikeType>()
val standardClassIdsType = type<StandardClassIds>()

val whenRefType = generatedType("", "FirExpressionRef")
    .withArgs(FirTreeBuilder.whenExpression)
val referenceToSimpleExpressionType = generatedType("", "FirExpressionRef")
    .withArgs(FirTreeBuilder.expression)
val safeCallCheckedSubjectReferenceType = generatedType("", "FirExpressionRef")
    .withArgs(FirTreeBuilder.checkedSafeCallSubject)

val firModuleDataType = type("fir", "FirModuleData")
val firImplicitTypeWithoutSourceType = generatedType("types.impl", "FirImplicitTypeRefImplWithoutSource")
val firQualifierPartType = type("fir.types", "FirQualifierPart")
val simpleNamedReferenceType = generatedType("references.impl", "FirSimpleNamedReference")
val explicitThisReferenceType = generatedType("references.impl", "FirExplicitThisReference")
val explicitSuperReferenceType = generatedType("references.impl", "FirExplicitSuperReference")
val implicitBooleanTypeRefType = generatedType("types.impl", "FirImplicitBooleanTypeRef")
val implicitNothingTypeRefType = generatedType("types.impl", "FirImplicitNothingTypeRef")
val implicitStringTypeRefType = generatedType("types.impl", "FirImplicitStringTypeRef")
val implicitUnitTypeRefType = generatedType("types.impl", "FirImplicitUnitTypeRef")
val resolvePhaseType = type("fir.declarations", "FirResolvePhase")
val resolveStateType = type("fir.declarations", "FirResolveState")
val propertyBodyResolveStateType = type("fir.declarations", "FirPropertyBodyResolveState")
val stubReferenceType = generatedType("references.impl", "FirStubReference")

val firBasedSymbolType = type("fir.symbols", "FirBasedSymbol")
val functionSymbolType = type("fir.symbols.impl", "FirFunctionSymbol")
val backingFieldSymbolType = type("fir.symbols.impl", "FirBackingFieldSymbol")
val delegateFieldSymbolType = type("fir.symbols.impl", "FirDelegateFieldSymbol")
val classLikeSymbolType = type("fir.symbols.impl", "FirClassLikeSymbol").withArgs(TypeRef.Star)
val regularClassSymbolType = type("fir.symbols.impl", "FirRegularClassSymbol")
val typeParameterSymbolType = type("fir.symbols.impl", "FirTypeParameterSymbol")
val emptyArgumentListType = type("fir.expressions", "FirEmptyArgumentList")
val firScopeProviderType = type("fir.scopes", "FirScopeProvider")

val pureAbstractElementType = generatedType("FirPureAbstractElement")
val coneContractElementType = type("fir.contracts.description", "ConeContractDescriptionElement")
val coneEffectDeclarationType = type("fir.contracts.description", "ConeEffectDeclaration")
val coneDiagnosticType = generatedType("diagnostics", "ConeDiagnostic", kind = TypeKind.Interface)
val coneStubDiagnosticType = generatedType("diagnostics", "ConeStubDiagnostic")

val firImplementationDetailType = generatedType("FirImplementationDetail")
val declarationOriginType = generatedType("declarations", "FirDeclarationOrigin")
val declarationAttributesType = generatedType("declarations", "FirDeclarationAttributes")

val exhaustivenessStatusType = generatedType("expressions", "ExhaustivenessStatus")

val callableReferenceMappedArgumentsType = type("fir.resolve.calls", "CallableReferenceMappedArguments")

val functionCallOrigin = type("fir.expressions", "FirFunctionCallOrigin")

val resolvedDeclarationStatusImplType = type("fir.declarations.impl", "FirResolvedDeclarationStatusImpl")

val deprecationsProviderType = type("fir.declarations", "DeprecationsProvider")
val unresolvedDeprecationsProviderType = type("fir.declarations", "UnresolvedDeprecationProvider")
val emptyAnnotationArgumentMappingType = type("fir.expressions.impl", "FirEmptyAnnotationArgumentMapping")

val firPropertySymbolType = type("fir.symbols.impl", "FirPropertySymbol")
val errorTypeRefImplType = type("fir.types.impl", "FirErrorTypeRefImpl")

val annotationResolvePhaseType = generatedType("expressions", "FirAnnotationResolvePhase")

val typeRefMarkerType = type("mpp", "TypeRefMarker")

val firVisitorType = generatedType("visitors", "FirVisitor")
val firVisitorVoidType = generatedType("visitors", "FirVisitorVoid")
val firDefaultVisitorType = generatedType("visitors", "FirDefaultVisitor")
val firDefaultVisitorVoidType = generatedType("visitors", "FirDefaultVisitorVoid")
val firTransformerType = generatedType("visitors", "FirTransformer")

val resolveStateAccessAnnotation = type("fir.declarations", "ResolveStateAccess", kind = TypeKind.Class)
val unresolvedExpressionTypeAccessAnnotation = type("fir.expressions", "UnresolvedExpressionTypeAccess", kind = TypeKind.Class)
val rawFirApi = type("fir.expressions", "RawFirApi", kind = TypeKind.Class)
val firBuilderDslAnnotation = type("fir.builder", "FirBuilderDsl", kind = TypeKind.Class)
