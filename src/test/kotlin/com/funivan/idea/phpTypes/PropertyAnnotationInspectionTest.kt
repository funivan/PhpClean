package com.funivan.idea.phpTypes

import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.lang.inspections.PhpInspection


class PropertyAnnotationInspectionTest : LightCodeInsightFixtureTestCase() {

    fun testPrivateProperties() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class A {
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

                        /**
                         * @var string|null
                         */
                        private ${'$'}second;
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