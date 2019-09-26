package com.funivan.idea.phpClean.inspections.globalVariableUsage

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class GlobalVariableUsageInspection : PhpCleanInspection() {
    val names = hashSetOf(
            "GLOBALS",
            "_SERVER",
            "_REQUEST",
            "_POST",
            "_GET",
            "_FILES",
            "_ENV",
            "_COOKIE",
            "_SESSION"
    )

    override fun getShortName() = "GlobalVariableUsageInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpVariable(variable: Variable) {
                if (names.contains(variable.name)) {
                    holder.registerProblem(
                            variable,
                            "Deprecated global variable usage"
                    )
                }
            }
        }
    }


}