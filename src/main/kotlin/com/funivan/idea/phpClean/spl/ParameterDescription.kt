package com.funivan.idea.phpClean.spl

import com.jetbrains.php.lang.psi.elements.Method


class ParameterDescription(private val method: Method) {
    private val description = lazy { description() }

    fun get(name: String): String {
        return description.value.get(name) ?: ""
    }

    private fun description(): HashMap<String, String> {
        val comment = method.docComment
        val result = hashMapOf<String, String>()
        if (comment != null) {
            for (tag in comment.paramTags) {
                val name = tag.varName
                val description = tag.tagValue
                if (name != null) {
                    result.set(name, description)
                }
            }
        }
        return result
    }
}