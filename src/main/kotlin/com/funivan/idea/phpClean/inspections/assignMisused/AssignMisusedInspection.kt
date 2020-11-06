package com.funivan.idea.phpClean.inspections.assignMisused

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.tree.TokenSet
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.AssignmentExpression
import com.jetbrains.php.lang.psi.elements.BinaryExpression
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class AssignMisusedInspection : PhpCleanInspection() {
    override fun getShortName() = "AssignMisusedInspection"
    val tokens = lazy {
        TokenSet.create(
                PhpTokenTypes.opEQUAL,
                PhpTokenTypes.opNOT_EQUAL,
                PhpTokenTypes.opIDENTICAL,
                PhpTokenTypes.opNOT_IDENTICAL
        )
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpBinaryExpression(expression: BinaryExpression) {
                val operator = expression.operationType
                if (
                        tokens.value.contains(operator)
                        && expression.lastChild is AssignmentExpression
                ) {
                    holder.registerProblem(
                            expression,
                            "Assignment and comparison operators used in one statement"
                    )
                }
            }
        }
    }
}
