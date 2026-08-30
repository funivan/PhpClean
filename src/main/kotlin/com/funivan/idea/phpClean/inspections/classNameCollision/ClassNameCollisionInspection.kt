package com.funivan.idea.phpClean.inspections.classNameCollision

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.ui.SingleCheckboxOptionsPanel
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
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
    ): PhpClass? {
        val vendorDir = if (ignoreVendorClasses) findVendorDir(origin) else null
        return PhpIndex.getInstance(origin.project)
            .getClassesByName(name.text)
            .filter { it.fqn != origin.fqn }
            .filterNot { ignoreVendorClasses && isVendorClass(it, vendorDir) }
            .firstOrNull()
    }

    private fun findVendorDir(phpClass: PhpClass): VirtualFile? {
        val basePath = phpClass.project.basePath
            ?: return null
        val projectDir = LocalFileSystem.getInstance().findFileByPath(basePath)
            ?: return null
        return projectDir.findChild("vendor")
    }

    private fun isVendorClass(phpClass: PhpClass, vendorDir: VirtualFile?): Boolean {
        val file = phpClass.containingFile.virtualFile
            ?: return false
        if (vendorDir == null) {
            return false
        }

        return VfsUtilCore.isAncestor(vendorDir, file, false)
    }
}
