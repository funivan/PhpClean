package com.funivan.idea.phpClean.qf.makeClassMemberPrivate

import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.PhpModifierList

class MakeClassMemberPrivateQF(
        private val pointer: SmartPsiElementPointer<PsiElement>,
        private val familyName: String
) : LocalQuickFix {
    override fun getFamilyName() = familyName
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = pointer.element
        if (element is PsiElement) {
            val elementToInsert = PhpPsiElementFactory.createFromText(project, PhpTokenTypes.kwPRIVATE, "private")
            if (elementToInsert !== null) {
                element.replace(elementToInsert)
            }
        }
    }

    companion object {
        fun create(modifier: PhpModifierList?, name: String) = modifier
                ?.node
                ?.findChildByType(PhpTokenTypes.kwPROTECTED)
                ?.psi
                ?.let {
                    MakeClassMemberPrivateQF(Pointer(it).create(), name)
                }
    }
}
