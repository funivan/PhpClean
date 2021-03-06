package com.funivan.idea.phpClean.inspections.classNameCollision

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class ClassNameCollisionInspection : PhpCleanInspection() {
    override fun getShortName() = "ClassNameCollisionInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(phpClass: PhpClass) {
                val name = phpClass.nameIdentifier
                if (name != null) {
                    val phpIndex = PhpIndex.getInstance(phpClass.project)
                    val classes = phpIndex.getClassesByName(name.text)
                    val first = classes.firstOrNull { it.fqn != phpClass.fqn }
                    if (first != null) {
                        holder.registerProblem(
                                name,
                                "Class name collision with ${first.fqn}"
                        )
                    }
                }
            }
        }
    }

}
