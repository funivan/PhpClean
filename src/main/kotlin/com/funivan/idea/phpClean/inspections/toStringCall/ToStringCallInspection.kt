package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.ui.SingleCheckboxOptionsPanel
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.*
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import javax.swing.JComponent


class ToStringCallInspection : PhpCleanInspection() {
    @JvmField
    var ignoreClassesWithToString = false

    val context = IsToStringContext()
    val safeCastTypes = lazy {
        PhpType.builder()
                .add(PhpType.NULL)
                .add(PhpType.STRING)
                .add(PhpType.FALSE).add(PhpType.BOOLEAN)
                .add(PhpType.INT).add(PhpType.FLOAT).add(PhpType.NUMBER).build()
    }

    override fun getShortName() = "ToStringCallInspection"

    override fun createOptionsPanel(): JComponent {
        return SingleCheckboxOptionsPanel(
                "Ignore classes with __toString",
                this,
                "ignoreClassesWithToString"
        )
    }

    /**
     * Redundant parentheses do not change what gets cast, so they must not hide the call:
     * `(string)($o->m())` means exactly the same as `(string)$o->m()`.
     */
    private fun isCastContext(element: PsiElement): Boolean {
        var parent = element.parent
        while (parent is ParenthesizedExpression) {
            parent = parent.parent
        }
        return parent != null && context.match(parent)
    }

    private fun ignored(type: PhpType, element: PsiElement): Boolean {
        return ignoreClassesWithToString && HasToStringMethod(element.project).match(type)
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpVariable(variable: Variable) {
                if (!isCastContext(variable)) {
                    return
                }
                if (!IsSingleClassType().match(variable)) {
                    return
                }
                if (ignored(variable.type, variable)) {
                    return
                }
                holder.registerProblem(
                        variable,
                        "Deprecated __toString call",
                        AddToStringCallQF(
                                Pointer(variable as PhpPsiElement).create()
                        )
                )
            }

            override fun visitPhpNewExpression(expression: NewExpression) {
                if (!isCastContext(expression)) {
                    return
                }
                if (ignored(expression.type, expression)) {
                    return
                }
                holder.registerProblem(
                        expression,
                        "Deprecated __toString call",
                        AddToStringCallQF(
                                Pointer(expression as PhpPsiElement).create()
                        )
                )
            }

            override fun visitPhpMethodReference(reference: MethodReference) {
                if (!isCastContext(reference)) {
                    return
                }
                val resolve = reference.resolve()
                if (resolve is Function && resolve.name != "__toString") {
                    val declaredType = resolve.declaredType
                    val types = declaredType.filter(safeCastTypes.value)
                    if (!types.isEmpty) {
                        if (ignored(types, reference)) {
                            return
                        }
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
    }
}
