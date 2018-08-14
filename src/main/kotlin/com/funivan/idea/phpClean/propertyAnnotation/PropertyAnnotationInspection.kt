package com.funivan.idea.phpClean.propertyAnnotation

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.SmartPointerManager
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class PropertyAnnotationInspection : PhpInspection() {
    override fun getShortName() = "PropertyAnnotationInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(clazz: PhpClass) {
                val properties = clazz.fields.filter { it.isPhysical && it.modifier.isPrivate }
                if (clazz.constructor === null) {
                    for (property in properties) {
                        val nameNode = property.nameNode
                        if (nameNode != null && property.children.isEmpty()) {
                            val comment = property.docComment
                            if (comment != null) {
                                val varTag = comment.varTag
                                if (varTag !== null) {
                                    val type = varTag.type
                                    val qf = AddNullTypeQF(
                                            SmartPointerManager.getInstance(varTag.project).createSmartPsiElementPointer(varTag)
                                    )
                                    if (!type.types.contains("\\null")) {
                                        holder.registerProblem(
                                                nameNode.psi,
                                                "Property is not annotated correctly. Add null type",
                                                qf
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                super.visitPhpClass(clazz)
            }
        }
    }
}