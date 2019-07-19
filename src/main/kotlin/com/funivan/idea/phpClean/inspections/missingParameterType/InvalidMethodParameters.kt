package com.funivan.idea.phpClean.inspections.missingParameterType

import com.funivan.idea.phpClean.spl.ParameterDescription
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.Parameter

class InvalidMethodParameters(private val method: Method, private val inspectionName: String) {
    private var parameters: List<Parameter>? = null
    fun method() = method
    fun parameters(): List<Parameter> {
        val result: List<Parameter>
        if (parameters != null) {
            result = parameters as List<Parameter>
        } else {
            val description = ParameterDescription(method)
            result = Parameters(method)
                    .filter { it.declaredType.size() == 0 }
                    .filter { !description.get(it.name).contains("@Suppress(${inspectionName})") }
                    .toList()
            parameters = result
        }
        return result
    }


}