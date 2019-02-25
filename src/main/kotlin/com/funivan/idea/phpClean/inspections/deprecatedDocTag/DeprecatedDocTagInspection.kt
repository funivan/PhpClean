package com.funivan.idea.phpClean.inspections.deprecatedDocTag

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.funivan.idea.phpClean.spl.jb.qf.RemoveTagQF
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.ui.ListEditForm
import com.intellij.psi.PsiElementVisitor
import com.intellij.util.ui.JBUI
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import javax.swing.JComponent

class DeprecatedDocTagInspection : PhpCleanInspection() {
    var tags: MutableList<String> = mutableListOf()
    override fun getShortName() = "DeprecatedDocTagInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpDocTag(tag: PhpDocTag) {
                if (tags.contains(tag.name.removePrefix("@"))) {
                    holder.registerProblem(
                            tag,
                            "Deprecated tag",
                            RemoveTagQF(Pointer(tag).create())
                    )
                }
            }
        }
    }


    override fun createOptionsPanel(): JComponent {
        val form = ListEditForm("Deprecated tags", this.tags)
        val panel = form.contentPanel
        panel.preferredSize = JBUI.size(150, 100)
        return panel
    }

}