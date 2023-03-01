package com.funivan.idea.phpClean.actions.useNamedConstructor

import com.intellij.usageView.UsageViewBundle
import com.intellij.usageView.UsageViewDescriptor
import com.jetbrains.php.lang.psi.elements.Method

class NamedConstructorUsageViewDescriptor(
    val constructor: Method,
    val target: Method
) : UsageViewDescriptor {
    override fun getElements() =
        arrayOf(constructor)

    override fun getProcessedElementsHeader() =
        "Use named constructor: " + target.name

    override fun getCodeReferencesText(usagesCount: Int, filesCount: Int) =
        UsageViewBundle.getReferencesString(usagesCount, filesCount)
}
