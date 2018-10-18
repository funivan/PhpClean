package com.funivan.idea.phpClean.inspections.architecture.returnType

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class MissingReturnTypeInspection : PhpInspection() {
    override fun getShortName() = "MissingReturnTypeInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpMethod(method: Method) {
                if (method.returnType == null) {
                    val name = method.nameNode?.psi;
                    if (name != null) {
                        holder.registerProblem(name, "Missing return type")
                    }

                }
            }
        }
    }
}