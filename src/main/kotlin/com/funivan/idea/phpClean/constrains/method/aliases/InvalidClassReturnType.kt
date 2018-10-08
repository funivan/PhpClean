package com.funivan.idea.phpClean.constrains.method.aliases

import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.Method


class InvalidClassReturnType : com.funivan.idea.phpClean.constrains.ConstrainInterface<Method> {

    override fun match(target: Method): Boolean {
        var result = false
        if (target.declaredType.isComplete) {
            val type = target.declaredType
                    .filterPrimitives().types
                    .firstOrNull { it != "\\stdClass" && it != "\\Generator" }
            if (type != null) {
                val classes = PhpIndex.getInstance(target.project).getAnyByFQN(type)
                if (classes.isNotEmpty() && !classes.first().isInterface) {
                    result = true
                }
            }
        }
        return result
    }
}