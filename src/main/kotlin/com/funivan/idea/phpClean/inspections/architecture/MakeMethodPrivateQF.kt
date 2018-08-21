package com.funivan.idea.phpClean.inspections.architecture

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.PhpPsiElementFactory


class MakeMethodPrivateQF(private val pointer: SmartPsiElementPointer<PsiElement>) : LocalQuickFix {
    override fun getFamilyName() = "Make method private"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = pointer.element
        if (element is PsiElement) {
            val elementToInsert = PhpPsiElementFactory.createFromText(project, PhpTokenTypes.kwPRIVATE, "private")
            if (elementToInsert !== null) {
                element.replace(elementToInsert)
            }
        }
    }
}