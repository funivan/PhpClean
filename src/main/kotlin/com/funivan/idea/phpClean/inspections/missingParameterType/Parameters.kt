package com.funivan.idea.phpClean.inspections.missingParameterType

import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.Parameter

class Parameters(private val method: Method) : Iterable<Parameter> {
    override fun iterator(): Iterator<Parameter> {
        return method.parameters
                .filter { it.name !== "" }
                .iterator()
    }
}
