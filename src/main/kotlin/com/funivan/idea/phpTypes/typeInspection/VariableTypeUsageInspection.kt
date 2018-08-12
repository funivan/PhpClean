package com.funivan.idea.phpTypes.typeInspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.elements.If
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class VariableTypeUsageInspection : PhpInspection() {

    override fun getShortName(): String {
        return "VariableTypeUsageInspection"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpFunction(function: Function) {
                println("\nfunction:: " + function.nameCS)
                val variables = PsiTreeUtil.findChildrenOfType(function, Variable::class.java)
                val ifConditions = PsiTreeUtil.findChildrenOfType(function, If::class.java)
                for (variable in variables) {
                    val instructions = TypeCheckActionsFinder(ifConditions).find(variable)
                    val el = variable.originalElement
                    println("var: ${variable.name} start ${el.textRange.startOffset}")
                    for (instruction in instructions) {
                        println("instruction: " + instruction.toString())
                    }

                }
            }
        }
    }
}