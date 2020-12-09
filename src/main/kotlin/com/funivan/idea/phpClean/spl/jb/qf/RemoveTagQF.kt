package com.funivan.idea.phpClean.spl.jb.qf

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.lexer.PhpTokenTypes


class RemoveTagQF(private val pointer: SmartPsiElementPointer<PhpDocTag>) : LocalQuickFix {
    override fun getFamilyName() = "Remove tag"
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = pointer.element
        if (element is PhpDocTag) {
            val space = element.prevSibling
            val parent = element.parent
            var asterisk = space
            if (space is PsiWhiteSpace) {
                asterisk = space.prevSibling
            }
            if (asterisk?.node?.elementType == PhpTokenTypes.DOC_LEADING_ASTERISK) {
                asterisk.delete()
            }
            element.delete()
            if(parent is PhpDocComment && parent.text.matches(Regex("[*\\s\\\\/]+"))){
                parent.delete()
            }
        }
    }
}
