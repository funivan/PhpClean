package com.funivan.idea.phpClean.inspections.missingParameterType


import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class MissingParameterTypeDeclarationInspection : PhpCleanInspection() {

    private val name = "MissingParameterTypeDeclarationInspection"

    override fun getShortName() = name


    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PhpElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(phpClass: PhpClass) {
                val parent by lazy {
                    ClassesByFqn(phpClass.project, Implementations(phpClass)).all()
                }
                val contains = fun(base: List<PhpClass>, name: String): Boolean {
                    return base.firstOrNull { it.findMethodByName(name) != null } != null
                }
                phpClass.ownMethods
                        .map { InvalidMethodParameters(it, name) }
                        .filter { it.parameters().isNotEmpty() }
                        .filter {
                            !contains(parent, it.method().name)
                        }
                        .flatMap { it.parameters() }
                        .forEach {
                            holder.registerProblem(it, "Missing parameter type")
                        }
            }
        }
    }
}