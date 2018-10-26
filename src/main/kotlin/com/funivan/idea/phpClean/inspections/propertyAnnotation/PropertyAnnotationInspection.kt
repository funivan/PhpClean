package com.funivan.idea.phpClean.inspections.propertyAnnotation

import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class PropertyAnnotationInspection : PhpInspection() {
    override fun getShortName() = "PropertyAnnotationInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(clazz: PhpClass) {
                var properties = clazz.ownFields.filter { it.isPhysical }
                if (!clazz.isFinal || clazz.extendsList.referenceElements.isNotEmpty()) {
                    properties = properties.filter { it.modifier.isPrivate }
                }
                var constructorInClass = false
                val clazzConstructor = clazz.constructor
                if (clazzConstructor != null) {
                    val inClass = clazzConstructor.containingClass
                    if (inClass != null) {
                        constructorInClass = inClass.fqn == clazz.fqn
                    }
                }
                if (!constructorInClass) {
                    for (property in properties) {
                        val nameNode = property.nameNode
                        if (nameNode != null && property.children.isEmpty()) {
                            val comment = property.docComment
                            if (comment != null) {
                                val varTag = comment.varTag
                                if (varTag !== null) {
                                    val type = varTag.type
                                    val qf = AddNullTypeQF(
                                            Pointer(varTag).create()
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