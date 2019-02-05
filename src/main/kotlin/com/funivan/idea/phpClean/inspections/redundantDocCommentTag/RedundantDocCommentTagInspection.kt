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
                val items = mutableListOf<Pair<PhpDocTag?, PhpType?>>()
                if (comment != null) {
                    items.add(Pair(
                            comment.returnTag,
                            function.returnType?.type
                    ))
                    for ((index, paramTag) in comment.paramTags.withIndex()) {
                        items.add(Pair(
                                paramTag, function.parameters.get(index + 1)?.type
                        ))
                    }
                    for (item in items) {
                        checkComment(item.first, item.second)
                    }
                }
            }

            fun checkComment(tag: PhpDocTag?, type: PhpType?) {
                if (tag != null && type != null && tag.tagValue == "") {
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