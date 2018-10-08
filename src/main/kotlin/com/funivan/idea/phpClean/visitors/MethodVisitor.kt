package com.funivan.idea.phpClean.visitors

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.intellij.codeInspection.ProblemsHolder
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class MethodVisitor(
        private val constraint: ConstrainInterface<Method>,
        private val message: String,
        private val holder: ProblemsHolder
) : PhpElementVisitor() {
    override fun visitPhpMethod(method: Method) {
        super.visitPhpMethod(method)
        val nameNode = method.nameNode
        if (nameNode != null && constraint.match(method)) {
            holder.registerProblem(nameNode.psi, message)
        }
    }
}