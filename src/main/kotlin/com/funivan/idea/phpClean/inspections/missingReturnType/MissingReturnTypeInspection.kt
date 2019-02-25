package com.funivan.idea.phpClean.inspections.missingReturnType

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class MissingReturnTypeInspection : PhpCleanInspection() {
    private val skip = hashSetOf("__construct", "__clone", "__destruct")
    override fun getShortName() = "MissingReturnTypeInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpMethod(method: Method) {
                if (method.returnType == null) {
                    val name = method.nameNode?.psi
                    if (name != null && !skip.contains(name.text)) {
                        holder.registerProblem(name, "Missing return type")
                    }
                }
            }
        }
    }
}