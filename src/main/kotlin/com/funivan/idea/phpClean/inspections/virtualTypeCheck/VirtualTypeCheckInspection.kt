package com.funivan.idea.phpClean.inspections.virtualTypeCheck

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocVariable
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.elements.AssignmentExpression
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class VirtualTypeCheckInspection : PhpCleanInspection() {
    override fun getShortName() = "VirtualTypeCheckInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpDocTag(tag: PhpDocTag) {
                if (tag.children.size == 3 && tag.name == "@var") {
                    val variable = tag.children.firstOrNull { it is PhpDocVariable }
                    val variableType = tag.children.firstOrNull { it is PhpDocType }
                    if (variable is PhpDocVariable && variableType is PhpDocType) {
                        val type = variableType.type
                        if (!type.isAmbiguous && type.types.size == 1 && !variableType.text.contains("<")) {
                            val plain = type.toStringResolved()
                            if (!PhpType.isPluralType(plain) && !PhpType.isNotExtendablePrimitiveType(plain)) {
                                val index = PhpIndex.getInstance(variableType.project)
                                if (index.getClassesByFQN(plain).isNotEmpty() || index.getInterfacesByFQN(plain)
                                        .isNotEmpty()
                                ) {
                                    val nextPsiSibling = (tag.parent as? PhpDocComment)?.nextPsiSibling?.firstChild
                                    if (nextPsiSibling is AssignmentExpression && nextPsiSibling.variable?.name == variable.name) {
                                        holder.registerProblem(
                                            variableType,
                                            "Use assert to check variable type"
                                        )
                                    } else {
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
}
