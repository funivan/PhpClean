package com.funivan.idea.phpClean.inspections.redundantDocCommentTag

import com.funivan.idea.phpClean.inspections.redundantDocCommentTag.tags.ParameterInfo
import com.funivan.idea.phpClean.inspections.redundantDocCommentTag.tags.ParameterType
import com.funivan.idea.phpClean.inspections.redundantDocCommentTag.tags.ReturnType
import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.funivan.idea.phpClean.spl.jb.qf.RemoveTagQF
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class RedundantDocCommentTagInspection : PhpCleanInspection() {
    override fun getShortName() = "RedundantDocCommentTagInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpMethod(method: Method) {
                visitPhpFunction(method)
            }

            override fun visitPhpFunction(function: Function) {
                val comment = function.docComment
                val items = mutableListOf<ParameterType>()
                if (comment != null) {
                    items.add(ReturnType(comment.returnTag, function))
                    for (paramTag in comment.paramTags) {
                        items.add(ParameterInfo(paramTag, function))
                    }
                    for (item in items) {
                        checkComment(item.doc(), item.type())
                    }
                }
            }

            override fun visitPhpField(field: Field) {
                val comment = field.docComment
                if (comment != null) {
                    for (paramTag in comment.paramTags) {
                        checkComment(paramTag, field.declaredType)
                    }
                }
            }

            fun checkComment(tag: PhpDocTag?, type: PhpType?) {
                if (tag != null && type != null && tag.tagValue == "") {
                    val doc = tag.type.toStringResolved()
                    val declared = type.toStringResolved()
                    if (doc == declared) {
                        val skip = (tag.firstPsiChild as? PhpDocType)?.canonicalText?.contains(Regex("[<\\]]+"))
                        if (skip == false) {
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
}
