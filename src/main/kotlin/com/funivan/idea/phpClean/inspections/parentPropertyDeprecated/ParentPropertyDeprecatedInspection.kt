package com.funivan.idea.phpClean.inspections.parentPropertyDeprecated

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class ParentPropertyDeprecatedInspection : PhpCleanInspection() {
    override fun getShortName() = "ParentPropertyDeprecatedInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpField(field: Field) {
                val nameNode = field.nameNode
                if (!field.isConstant && !field.isDeprecated && nameNode != null) {
                    field.containingClass
                            ?.superClass
                            ?.findOwnFieldByName(field.name, false)
                            ?.let {
                                if (it.isDeprecated) {
                                    holder.registerProblem(nameNode.psi, "Parent property is deprecated")
                                }
                            }
                }
            }
        }
    }
}
