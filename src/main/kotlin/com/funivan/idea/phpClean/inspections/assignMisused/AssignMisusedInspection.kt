package com.funivan.idea.phpClean.inspections.assignMisused

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.AssignmentExpression
import com.jetbrains.php.lang.psi.elements.BinaryExpression
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class AssignMisusedInspection : PhpCleanInspection() {
    override fun getShortName() = "AssignMisusedInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            fun error(expression: PsiElement) {
                holder.registerProblem(
                        expression,
                        "Assignment and comparison operators used in one statement"
                )
            }

            override fun visitPhpAssignmentExpression(expression: AssignmentExpression) {
                if (expression.lastChild is BinaryExpression) {
                    error(expression)
                }
                if (expression.parent is BinaryExpression) {
                    error(expression.parent)
                }
            }
        }
    }
}
