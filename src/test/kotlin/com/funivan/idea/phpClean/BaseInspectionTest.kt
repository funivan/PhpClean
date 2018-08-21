package com.funivan.idea.phpClean

import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.lang.inspections.PhpInspection


abstract class BaseInspectionTest : LightCodeInsightFixtureTestCase() {
    fun assert(inspection: PhpInspection, code: String, fixed: String? = null) {
        myFixture.configureByText(PhpFileType.INSTANCE, code)
        PsiDocumentManager.getInstance(project).commitAllDocuments()
        myFixture.enableInspections(inspection)
        myFixture.testHighlighting(true, false, true)
        if (fixed != null) {
            myFixture.getAllQuickFixes().forEach { myFixture.launchAction(it) }
            myFixture.checkResult(fixed)
        }
    }
}