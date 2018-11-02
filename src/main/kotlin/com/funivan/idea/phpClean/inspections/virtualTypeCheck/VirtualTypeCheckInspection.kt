package com.funivan.idea.phpClean.inspections.virtualTypeCheck

import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocVariable
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class VirtualTypeCheckInspection : PhpInspection() {
    override fun getShortName() = "VirtualTypeCheckInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpDocTag(tag: PhpDocTag) {
                if (tag.children.size == 3 && tag.name == "@var") {
                    val variable = tag.children.firstOrNull { it is PhpDocVariable }
                    val variableType = tag.children.firstOrNull { it is PhpDocType }
                    if (variable is PhpDocVariable && variableType is PhpDocType) {
                        val type = variableType.type
                        if (!type.isUndefined && type.types.size == 1) {
                            val plain = type.toStringResolved()
                            if (!PhpType.isPluralType(plain) && !PhpType.isNotExtendablePrimitiveType(plain)) {
                                if (PhpIndex.getInstance(variableType.project).getClassesByFQN(plain).isNotEmpty()) {
                                    holder.registerProblem(
                                            variableType,
                                            "Use assert to check variable type",
                                            UseAssertQF(
                                                    Pointer(tag.parent).create(),
                                                    Pointer(variable).create(),
                                                    Pointer(variableType).create()
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}