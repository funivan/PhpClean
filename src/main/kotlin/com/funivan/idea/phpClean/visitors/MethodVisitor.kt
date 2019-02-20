package com.funivan.idea.phpClean.visitors

import com.intellij.codeInspection.ProblemsHolder
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class MethodVisitor(
        private val valid: (Method) -> Boolean,
        private val message: String,
        private val holder: ProblemsHolder
) : PhpElementVisitor() {
    override fun visitPhpMethod(method: Method) {
        super.visitPhpMethod(method)
        val nameNode = method.nameNode
        if (nameNode != null && valid(method)) {
            holder.registerProblem(nameNode.psi, message)
        }
    }
}