package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.*
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class ToStringCallInspection : PhpCleanInspection() {
    val context = IsToStringContext()
    override fun getShortName() = "ToStringCallInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpVariable(variable: Variable) {
                if (context.match(variable.parent)) {
                    if (IsSingleClassType().match(variable)) {
                        holder.registerProblem(
                                variable,
                                "Deprecated __toString call",
                                AddToStringCallQF(
                                        Pointer(variable as PhpPsiElement).create()
                                )
                        )
                    }
                }
            }

            override fun visitPhpNewExpression(expression: NewExpression) {
                val parent = expression.parent
                if (context.match(parent)) {
                    holder.registerProblem(
                            expression,
                            "Deprecated __toString call",
                            AddToStringCallQF(
                                    Pointer(expression as PhpPsiElement).create()
                            )
                    )
                }
            }

            override fun visitPhpFunctionCall(reference: FunctionReference) {
                if (context.match(reference.parent)) {
                    val resolve = reference.resolve()
                    if (resolve is Function && resolve.name != "__toString") {
                        val types = resolve.declaredType.typesSorted.joinToString(separator="|")
                        val safeType = (listOf("\\string", "\\int", "\\float", "\\null|\\string", "\\null|\\int", "\\null|\\float").contains(types))
                        if (!safeType) {
                            holder.registerProblem(
                                    reference,
                                    "Deprecated __toString call",
                                    AddToStringCallQF(
                                            Pointer(reference as PhpPsiElement).create()
                                    )
                            )
                        }
                    }
                }
            }

            override fun visitPhpMethodReference(reference: MethodReference) {
                visitPhpFunctionCall(reference)
            }
        }
    }
}
