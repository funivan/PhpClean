package com.funivan.idea.phpClean.inspections.propertyCanBePrivate

import com.funivan.idea.phpClean.constrains.clazz.IsAloneClass
import com.funivan.idea.phpClean.qf.makeClassMemberPrivate.MakeClassMemberPrivateQF
import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpModifierList
import com.jetbrains.php.lang.psi.elements.impl.PhpPromotedFieldParameterImpl
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class PropertyCanBePrivateInspection : PhpCleanInspection() {
    val constraint = IsAloneClass()
    override fun getShortName() = "PropertyCanBePrivateInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(target: PhpClass) {
                if (constraint.match(target)) {
                    for (property in target.ownFields.filter { it.modifier.isProtected }) {
                        holder.registerProblem(
                            property.nameIdentifier ?: property,
                            "Property can be private",
                            qf(property)
                        )
                    }
                }
            }

            private fun qf(property: Field?): MakeClassMemberPrivateQF? {
                val qf = when (property is PhpPromotedFieldParameterImpl) {
                    true -> MakeClassMemberPrivateQF.create(property, "Make property private")
                    false -> MakeClassMemberPrivateQF.create(
                        property?.parent?.firstChild as? PhpModifierList,
                        "Make property private"
                    )
                }
                return qf
            }
        }
    }
}
