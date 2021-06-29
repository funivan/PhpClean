package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.*


class IsToStringContext : ConstrainInterface<PsiElement> {
    override fun match(target: PsiElement): Boolean {
        return (target is PhpEchoStatement)
                || (target is PhpPrintExpression)
                || (target is ConcatenationExpression)
                || (target is UnaryExpression && target.operation?.node?.elementType == PhpTokenTypes.opSTRING_CAST)
                || (
                target is BinaryExpression
                        && target.operation?.node?.elementType == PhpTokenTypes.opEQUAL
                        && (target.leftOperand is StringLiteralExpression || target.rightOperand is StringLiteralExpression)
                )
    }
}
