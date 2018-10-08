package com.funivan.idea.phpClean.visitors

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.intellij.codeInspection.ProblemsHolder
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class ParameterVisitor(
        private val constraint: ConstrainInterface<Parameter>,
        private val message: String,
        private val holder: ProblemsHolder
) : PhpElementVisitor() {
    override fun visitPhpParameter(parameter: Parameter) {
        super.visitPhpParameter(parameter)
        if (constraint.match(parameter)) {
            holder.registerProblem(parameter, message)
        }
    }
}