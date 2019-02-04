package com.funivan.idea.phpClean.inspections.redundantDocCommentTag

import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class RedundantDocCommentTagInspection : PhpInspection() {
    override fun getShortName() = "RedundantDocCommentTagInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpFunction(function: Function) {
                val comment = function.docComment
                if (comment != null) {
                    val returnTag = comment.returnTag
                    val returnType = function.returnType
                    if (returnTag !== null && returnType !== null) {
                        checkComment(returnTag, returnType.type)
                    }
                }
            }

            fun checkComment(tag: PhpDocTag, type: PhpType) {
                if (tag.tagValue == "") {
                    val first = tag.firstPsiChild
                    if (first is PhpDocType && first.type.toStringResolved() == type.toStringResolved()) {
                        holder.registerProblem(
                                tag,
                                "Redundant PhpDoc tag",
                                RemoveTagQF(Pointer(tag).create())
                        )
                    }

                }
            }
        }
    }
}