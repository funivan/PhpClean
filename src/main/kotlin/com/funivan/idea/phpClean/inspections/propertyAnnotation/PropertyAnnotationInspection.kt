package com.funivan.idea.phpClean.inspections.propertyAnnotation

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.AssignmentExpression
import com.jetbrains.php.lang.psi.elements.FieldReference
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class PropertyAnnotationInspection : PhpCleanInspection() {
    override fun getShortName() = "PropertyAnnotationInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(clazz: PhpClass) {
                val clazzConstructor = clazz.constructor
                val propertiesInitedInConstructor = mutableListOf<String?>()
                if (clazzConstructor != null) {
                    PsiTreeUtil.processElements(clazzConstructor) {
                        if (it is AssignmentExpression) {
                            val variable = it.variable
                            if (variable is FieldReference) {
                                propertiesInitedInConstructor.add(variable.name)
                            }
                        }
                        return@processElements true
                    }
                }
                var properties = clazz.ownFields.filter {
                    it.isPhysical && !propertiesInitedInConstructor.contains(it.name)
                }
                if (!clazz.isFinal || clazz.extendsList.referenceElements.isNotEmpty()) {
                    properties = properties.filter { it.modifier.isPrivate }
                }
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
                super.visitPhpClass(clazz)
            }
        }
    }
}