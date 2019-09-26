package com.funivan.idea.phpClean.inspections.toStringCall

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.NewExpression
import com.jetbrains.php.lang.psi.elements.PhpPsiElement


class AddToStringCallQF(private val pointer: SmartPsiElementPointer<PhpPsiElement>) : LocalQuickFix {
    override fun getFamilyName() = "Add __toString call"
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = pointer.element
        val braces = element is NewExpression
        if (element is PhpPsiElement) {
            var text = element.text
            if (braces) {
                text = "($text)"
            }
            val fn = PhpPsiElementFactory.createMethodReference(project, "$text->__toString()")
            element.replace(fn)
        }
    }
}