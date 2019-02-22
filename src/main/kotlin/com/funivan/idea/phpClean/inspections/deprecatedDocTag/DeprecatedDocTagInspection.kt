package com.funivan.idea.phpClean.inspections.deprecatedDocTag

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.ui.ListEditForm
import com.intellij.psi.PsiElementVisitor
import com.intellij.util.ui.JBUI
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import java.util.*
import javax.swing.JComponent

class DeprecatedDocTagInspection : PhpInspection() {
    var tags: MutableList<String> = mutableListOf()
    override fun getShortName() = "DeprecatedDocTagInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpDocTag(tag: PhpDocTag) {
                if (tags.contains(tag.name.removePrefix("@"))) {
                    holder.registerProblem(
                            tag,
                            "Deprecated tag"
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