package com.funivan.idea.phpClean.inspections.toStringCall

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.NewExpression
import com.jetbrains.php.lang.psi.elements.PhpEchoStatement
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class ToStringCallInspection : PhpInspection() {
    override fun getShortName() = "ToStringCallInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpNewExpression(expression: NewExpression) {
                val parent = expression.parent
                if (parent is PhpEchoStatement) {
                    holder.registerProblem(
                            parent.firstChild ?: expression,
                            "Deprecated __toString call"
                    )
                }
            }

            override fun visitPhpMethodReference(reference: MethodReference) {
                val parent = reference.parent
                if (parent is PhpEchoStatement) {
                    val resolve = reference.resolve()
                    if (resolve is Method) {
                        val type = resolve.returnType
                        var safeType = false
                        if (type == null || listOf("\\string", "\\int", "\\float").contains(type.classReference.fqn)) {
                            safeType = true
                        }
                        if (!safeType) {
                            holder.registerProblem(
                                    parent.firstChild ?: reference,
                                    "Deprecated __toString call"
                            )
                        }
                    }
                }
            }
        }
    }
}