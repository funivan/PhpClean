package com.funivan.idea.phpClean.inspections.methodShouldBeFinal

import com.funivan.idea.phpClean.constrains.method.IsMagic
import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class MethodShouldBeFinalInspection : PhpCleanInspection() {
    private val magic = IsMagic()
    override fun getShortName() = "MethodShouldBeFinalInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(phpClass: PhpClass) {
                if (!phpClass.isFinal && !phpClass.isInterface) {
                    for (method in methods(phpClass)) {
                        holder.registerProblem(
                                method.nameIdentifier ?: method,
                                "Method should be final"
                        )
                    }
                }
            }
        }
    }

    private fun methods(clazz: PhpClass): List<Method> {
        return clazz.ownMethods.filter {
            !it.modifier.isFinal
                    && !it.modifier.isPrivate
                    && !it.modifier.isAbstract
                    && !it.modifier.isStatic
                    && !magic.match(it)
        }
    }
}