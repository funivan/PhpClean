package com.funivan.idea.phpClean.spl.ci

import com.intellij.util.ResourceUtil
import com.jetbrains.php.lang.inspections.PhpInspection


class ShortDescription(private val inspection: PhpInspection) {
    fun value(): String {
        val description = ResourceUtil.loadText(
                inspection.javaClass.getResource("/inspectionDescriptions/" + inspection.shortName + ".html")
        )
        val match = Regex("(.+)\n<!-- main -->", RegexOption.DOT_MATCHES_ALL).find(description)
        var result = ""
        if (match !== null) {
            result = match.groupValues.firstOrNull() ?: ""
        }
        return result
    }
}
