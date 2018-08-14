package com.funivan.idea.phpClean.propertyAnnotation

import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.lang.inspections.PhpInspection


class PropertyAnnotationInspectionTest : LightCodeInsightFixtureTestCase() {

    fun testPropertiesWithoutDocumentation() {
        assert(
            PropertyAnnotationInspection(),
            """
                    <?php
                    class A {
                     private ${'$'}name;
                     /**
                      * @deprecated
                      * some description
                      */
                     private ${'$'}id;
                    }
                    """
        )
    }

    fun testPropertyWithCorrectDocumentation() {
        assert(
            PropertyAnnotationInspection(),
            """
                    <?php
                    class A {
                        /** @var string[]|null */
                        private ${'$'}first;

                        /**
                         * @var string|null
                         */
                        private ${'$'}second;
                    }
                """
        )
    }

    fun testPropertiesInFinalClass() {
        assert(
            PropertyAnnotationInspection(),
            """
                    <?php
                    final class A {
                     /** @var string[]*/
                     public <warning descr="Property is not annotated correctly. Add null type">${'$'}names</warning>;
                     /** @var int */
                     protected <warning descr="Property is not annotated correctly. Add null type">${'$'}id</warning>;
                    }
                    """
        )
    }

    fun testPropertiesInFinalClassWithExtends() {
        assert(
            PropertyAnnotationInspection(),
            """
                    <?php
                    final class B extends C{
                     /** @var string */
                     protected ${'$'}id; // can be inited in the parent class
                     /** @var int */
                     private <warning descr="Property is not annotated correctly. Add null type">${'$'}age</warning>;
                    }
                    """
        )
    }

    fun testPrivatePropertiesInClassesWithoutConstructor() {
        assert(
            PropertyAnnotationInspection(),
            """
                    <?php
                    class A {
                        /** @var string */
                        private <warning descr="Property is not annotated correctly. Add null type">${'$'}first</warning>;
                    }
                """
        )
    }

    fun testPrivatePropertyNotInitializedInTheConstructor() {
        assert(
            PropertyAnnotationInspection(),
            """
                    <?php
                    class A {
                        /**
                         * @var string
                         */
                        private <warning descr="Property is not annotated correctly. Add null type">${'$'}first</warning>;
                    }
                """
        )
    }

    private fun assert(inspection: PhpInspection, code: String) {
        myFixture.configureByText(PhpFileType.INSTANCE, code)
        PsiDocumentManager.getInstance(project).commitAllDocuments()
        myFixture.enableInspections(inspection)
        myFixture.testHighlighting(true, false, true)
    }
}