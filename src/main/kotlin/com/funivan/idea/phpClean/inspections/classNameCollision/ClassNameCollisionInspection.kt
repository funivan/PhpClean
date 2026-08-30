package com.funivan.idea.phpClean.inspections.classNameCollision

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.ui.SingleCheckboxOptionsPanel
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import javax.swing.JComponent

class ClassNameCollisionInspection : PhpCleanInspection() {
    @JvmField
    var ignoreVendorClasses = false

    override fun getShortName() = "ClassNameCollisionInspection"

    override fun createOptionsPanel(): JComponent {
        return SingleCheckboxOptionsPanel(
            "Ignore vendor classes",
            this,
            "ignoreVendorClasses"
        )
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(phpClass: PhpClass) {
                phpClass.nameIdentifier?.let { name ->
                    find(phpClass, name)?.let { clazz ->
                        holder.registerProblem(
                            name,
                            "Class name collision with ${clazz.fqn}"
                        )
                    }
                }
            }
        }
    }

    private fun find(
        origin: PhpClass,
        name: PsiElement
    ) = PhpIndex.getInstance(origin.project)
        .getClassesByName(name.text)
        .firstOrNull { it.fqn != origin.fqn && !(ignoreVendorClasses && isVendorClass(it)) }

    private fun isVendorClass(phpClass: PhpClass): Boolean {
        return phpClass.containingFile.virtualFile?.path
            ?.replace('\\', '/')
            ?.contains("/vendor/") == true
    }
}
