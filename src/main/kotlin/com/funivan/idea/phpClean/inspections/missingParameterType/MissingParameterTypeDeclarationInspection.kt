package com.funivan.idea.phpClean.inspections.missingParameterType


import com.funivan.idea.phpClean.spl.ParameterDescription
import com.intellij.codeInspection.ProblemsHolder
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class MissingParameterTypeDeclarationInspection : PhpInspection() {

    private val name = "MissingParameterTypeDeclarationInspection"

    override fun getShortName() = name


    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PhpElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpMethod(method: Method) {
                val description = ParameterDescription(method)
                Parameters(method)
                        .filter { it.declaredType.size() == 0 }
                        .filter { !description.get(it.name).contains("@Suppress(${name})") }
                        .forEach {
                            holder.registerProblem(it, "Missing parameter type")
                        }
            }
        }
    }

}
