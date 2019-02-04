package com.funivan.idea.phpClean.inspections.redundantDocCommentTag

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag


class RemoveTagQF(private val pointer: SmartPsiElementPointer<PhpDocTag>) : LocalQuickFix {
    override fun getFamilyName() = "Remove tag"
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = pointer.element
        if (element is PhpDocTag) {
            element.delete()
        }
    }
}