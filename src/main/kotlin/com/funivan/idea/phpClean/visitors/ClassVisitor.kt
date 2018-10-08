package com.funivan.idea.phpClean.visitors

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.intellij.codeInspection.ProblemsHolder
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class ClassVisitor(
        private val constraint: ConstrainInterface<PhpClass>,
        private val message: String,
        private val holder: ProblemsHolder
) : PhpElementVisitor() {
    override fun visitPhpClass(target: PhpClass) {
        super.visitPhpClass(target)
        val nameNode = target.nameNode
        if (nameNode != null && constraint.match(target)) {
            holder.registerProblem(nameNode.psi, message)
        }
    }

}