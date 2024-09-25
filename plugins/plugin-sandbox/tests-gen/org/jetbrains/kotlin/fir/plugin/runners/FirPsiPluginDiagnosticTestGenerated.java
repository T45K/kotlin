/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.plugin.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/plugin-sandbox/testData/diagnostics")
@TestDataPath("$PROJECT_ROOT")
public class FirPsiPluginDiagnosticTestGenerated extends AbstractFirPsiPluginDiagnosticTest {
  @Test
  public void testAllFilesPresentInDiagnostics() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/plugin-sandbox/testData/diagnostics"), Pattern.compile("^(.+)\\.kt$"), null, true);
  }

  @Nested
  @TestMetadata("plugins/plugin-sandbox/testData/diagnostics/checkers")
  @TestDataPath("$PROJECT_ROOT")
  public class Checkers {
    @Test
    public void testAllFilesPresentInCheckers() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/plugin-sandbox/testData/diagnostics/checkers"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("dependencyWithoutAttributePlugin.kt")
    public void testDependencyWithoutAttributePlugin() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/checkers/dependencyWithoutAttributePlugin.kt");
    }

    @Test
    @TestMetadata("importsWithGeneratedDeclarations.kt")
    public void testImportsWithGeneratedDeclarations() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/checkers/importsWithGeneratedDeclarations.kt");
    }

    @Test
    @TestMetadata("mixingComposableAndNormalFunctions.kt")
    public void testMixingComposableAndNormalFunctions() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/checkers/mixingComposableAndNormalFunctions.kt");
    }

    @Test
    @TestMetadata("signedNumbersCheckers.kt")
    public void testSignedNumbersCheckers() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/checkers/signedNumbersCheckers.kt");
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/checkers/simple.kt");
    }

    @Test
    @TestMetadata("ValueOfCustomFunctionTypeAsArgumentOfInlineFunction.kt")
    public void testValueOfCustomFunctionTypeAsArgumentOfInlineFunction() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/checkers/ValueOfCustomFunctionTypeAsArgumentOfInlineFunction.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/plugin-sandbox/testData/diagnostics/functionalTypes")
  @TestDataPath("$PROJECT_ROOT")
  public class FunctionalTypes {
    @Test
    public void testAllFilesPresentInFunctionalTypes() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/plugin-sandbox/testData/diagnostics/functionalTypes"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("ambigousKinds.kt")
    public void testAmbigousKinds() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/functionalTypes/ambigousKinds.kt");
    }

    @Test
    @TestMetadata("dependencyWithoutFunctionalKindPlugin.kt")
    public void testDependencyWithoutFunctionalKindPlugin() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/functionalTypes/dependencyWithoutFunctionalKindPlugin.kt");
    }

    @Test
    @TestMetadata("inference.kt")
    public void testInference() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/functionalTypes/inference.kt");
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/functionalTypes/simple.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/plugin-sandbox/testData/diagnostics/memberGen")
  @TestDataPath("$PROJECT_ROOT")
  public class MemberGen {
    @Test
    public void testAllFilesPresentInMemberGen() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/plugin-sandbox/testData/diagnostics/memberGen"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("classWithCompanionObject.kt")
    public void testClassWithCompanionObject() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/memberGen/classWithCompanionObject.kt");
    }

    @Test
    @TestMetadata("classWithGeneratedMembersAndNestedClass.kt")
    public void testClassWithGeneratedMembersAndNestedClass() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/memberGen/classWithGeneratedMembersAndNestedClass.kt");
    }

    @Test
    @TestMetadata("generatedClassWithMembersAndNestedClasses.kt")
    public void testGeneratedClassWithMembersAndNestedClasses() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/memberGen/generatedClassWithMembersAndNestedClasses.kt");
    }

    @Test
    @TestMetadata("localClassWithCompanionObject.kt")
    public void testLocalClassWithCompanionObject() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/memberGen/localClassWithCompanionObject.kt");
    }

    @Test
    @TestMetadata("topLevelCallables.kt")
    public void testTopLevelCallables() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/memberGen/topLevelCallables.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/plugin-sandbox/testData/diagnostics/receivers")
  @TestDataPath("$PROJECT_ROOT")
  public class Receivers {
    @Test
    public void testAllFilesPresentInReceivers() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/plugin-sandbox/testData/diagnostics/receivers"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("callShapeBasedInjector.kt")
    public void testCallShapeBasedInjector() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/receivers/callShapeBasedInjector.kt");
    }

    @Test
    @TestMetadata("receiverInjection.kt")
    public void testReceiverInjection() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/receivers/receiverInjection.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/plugin-sandbox/testData/diagnostics/status")
  @TestDataPath("$PROJECT_ROOT")
  public class Status {
    @Test
    public void testAllFilesPresentInStatus() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/plugin-sandbox/testData/diagnostics/status"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("metaAnnotation.kt")
    public void testMetaAnnotation() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/status/metaAnnotation.kt");
    }

    @Test
    @TestMetadata("metaAnnotationClashesWithSupertype.kt")
    public void testMetaAnnotationClashesWithSupertype() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/status/metaAnnotationClashesWithSupertype.kt");
    }

    @Test
    @TestMetadata("recursiveAnnotation.kt")
    public void testRecursiveAnnotation() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/status/recursiveAnnotation.kt");
    }

    @Test
    @TestMetadata("redundantTransformedVisibility.kt")
    public void testRedundantTransformedVisibility() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/status/redundantTransformedVisibility.kt");
    }

    @Test
    @TestMetadata("simpleAnnotation.kt")
    public void testSimpleAnnotation() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/status/simpleAnnotation.kt");
    }

    @Test
    @TestMetadata("visibilityTransformation.kt")
    public void testVisibilityTransformation() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/status/visibilityTransformation.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/plugin-sandbox/testData/diagnostics/supertypes")
  @TestDataPath("$PROJECT_ROOT")
  public class Supertypes {
    @Test
    public void testAllFilesPresentInSupertypes() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/plugin-sandbox/testData/diagnostics/supertypes"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("entityTypeSuperClass.kt")
    public void testEntityTypeSuperClass() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/supertypes/entityTypeSuperClass.kt");
    }

    @Test
    @TestMetadata("metaAnnotationOrder.kt")
    public void testMetaAnnotationOrder() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/supertypes/metaAnnotationOrder.kt");
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/supertypes/simple.kt");
    }

    @Test
    @TestMetadata("supertypeWithArgument.kt")
    public void testSupertypeWithArgument() {
      runTest("plugins/plugin-sandbox/testData/diagnostics/supertypes/supertypeWithArgument.kt");
    }
  }
}
