package com.funivan.idea.phpClean.inspections.propertyAnnotation

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.PhpPsiElementFactory


class AddNullTypeQF(private val pointer: SmartPsiElementPointer<PhpDocParamTag>) : LocalQuickFix {
    override fun getFamilyName() = "Add null type"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = pointer.element
        if (element is PhpDocTag) {
            val newText = element.text.replace(Regex("(@var[ ]+)([^ ]+)( |$)"), "$1$2|null$3")
            val elementToInsert = PhpPsiElementFactory.createFromText(project, PhpDocTag::class.java, "/** $newText */\\n")
            if (elementToInsert !== null) {
                element.replace(elementToInsert)
            }
        }
    }
}