package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.ConcatenationExpression
import com.jetbrains.php.lang.psi.elements.PhpEchoStatement


class IsToStringContext : ConstrainInterface<PsiElement> {
    override fun match(target: PsiElement): Boolean {
        return target is PhpEchoStatement || target is ConcatenationExpression
    }
}