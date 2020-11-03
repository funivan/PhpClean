package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.*
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class ToStringCallInspection : PhpCleanInspection() {
    val context = IsToStringContext()
    val safeCastTypes = lazy {
        PhpType.builder()
                .add(PhpType.NULL)
                .add(PhpType.STRING)
                .add(PhpType.FALSE).add(PhpType.BOOLEAN)
                .add(PhpType.INT).add(PhpType.FLOAT).add(PhpType.NUMBER).build()
    }

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
                        val declaredType = resolve.declaredType
                        val types = declaredType.filter(safeCastTypes.value)
                        if (!types.isEmpty) {
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
