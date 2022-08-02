package com.funivan.idea.phpClean.inspections.classNameCollision

import com.funivan.idea.phpClean.inspections.missingParameterType.ClassesByFqn
import com.funivan.idea.phpClean.inspections.missingParameterType.Implementations
import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class ProhibitedClassExtendInspection : PhpCleanInspection() {
    override fun getShortName() = "ProhibitedClassExtendInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

        return object : PhpElementVisitor() {
            private fun parent(phpClass: PhpClass) = ClassesByFqn(phpClass.project, Implementations(phpClass)).all()
            private fun hasFinalTag(phpClass: PhpClass): Boolean {
                val docComment = phpClass.docComment
                return when (docComment is PhpDocComment) {
                    true -> docComment.getTagElementsByName("@final").isNotEmpty()
                    false -> false
                }
            }

            override fun visitPhpClass(phpClass: PhpClass) {
                phpClass.nameIdentifier?.let { name ->
                    parent(phpClass).forEach { extendClass ->
                        if (hasFinalTag(extendClass)) {
                            holder.registerProblem(
                                name,
                                "Prohibited extentions of @final class ${extendClass.fqn}"
                            )
                        }
                    }
                }
            }
        }
    }
}
