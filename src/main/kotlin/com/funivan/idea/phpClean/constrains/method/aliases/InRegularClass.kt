package com.funivan.idea.phpClean.constrains.method.aliases

import com.jetbrains.php.lang.psi.elements.Method


class InRegularClass : com.funivan.idea.phpClean.constrains.ConstrainInterface<Method> {
    override fun match(target: Method): Boolean {
        val baseClass = target.containingClass
        var result = false
        if (baseClass != null) {
            result = !baseClass.isAbstract && !baseClass.isInterface && !baseClass.isTrait && !baseClass.isAnonymous
        }
        return result
    }
}