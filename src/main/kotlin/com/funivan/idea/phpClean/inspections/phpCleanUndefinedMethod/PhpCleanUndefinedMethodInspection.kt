package com.funivan.idea.phpClean.inspections.phpCleanUndefinedMethod

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class PhpCleanUndefinedMethodInspection : PhpCleanInspection() {
    override fun getShortName() = "PhpCleanUndefinedMethodInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpMethodReference(reference: MethodReference) {
                val classReference = reference.classReference
                if (classReference is Variable && classReference.type.types.any { it == "\\mixed" }) {
                    val name = reference.nameNode
                    if (name !== null && reference.resolve() == null) {
                        holder.registerProblem(name.psi, "Undefined method")
                    }
                }
            }
        }
    }
}