package com.funivan.idea.phpClean.spl.jb.qf

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.lexer.PhpTokenTypes


class RemoveTagQF(private val pointer: SmartPsiElementPointer<PhpDocTag>) : LocalQuickFix {
    override fun getFamilyName() = "Remove tag"
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = pointer.element
        if (element is PhpDocTag) {
            val removal = mutableListOf<PsiElement>(element)
            val space = element.prevSibling
            var asterisk = space
            if (isTypeOf(space, PhpTokenTypes.WHITE_SPACE)) {
                removal.add(space)
                asterisk = space.prevSibling
            }
            if (isTypeOf(asterisk, PhpTokenTypes.DOC_LEADING_ASTERISK)) {
                removal.add(asterisk)
            }
            removal.forEach { it.delete() }
        }
    }
}

private fun isTypeOf(element: PsiElement?, type: IElementType): Boolean {
    return element?.node?.elementType == type
}
