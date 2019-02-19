package com.funivan.idea.phpClean.inspections.deprecatedDocTag

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import org.jdom.Element
import java.util.*
import javax.swing.JComponent

class DeprecatedDocTagInspection : PhpInspection() {
    val tagNames = mutableListOf<String>()


    override fun getShortName() = "DeprecatedDocTagInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpDocTag(tag: PhpDocTag) {
                println(tag.nameCS)
                super.visitPhpDocTag(tag)
            }
        }
    }

    override fun createOptionsPanel(): JComponent? {
        return LabeledComponent.create(
                TagNamesListPanel(tagNames),
                "Deprecated tags"
        )
    }

//    companion object {
//        val USE_PROPERTY_ACCESS_INSPECTION: Key<DeprecatedDocTagInspection> = Key.create("DeprecatedDocTagInspection")
//    }

    override fun readSettings(node: Element) {
        super.readSettings(node)
        val uniqueEntries = HashSet<String>(tagNames)
        tagNames.clear()
        tagNames.addAll(uniqueEntries)
    }

}