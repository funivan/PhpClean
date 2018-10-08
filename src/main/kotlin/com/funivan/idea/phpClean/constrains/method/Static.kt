package com.funivan.idea.phpClean.constrains.method

import com.jetbrains.php.lang.psi.elements.Method


class Static : com.funivan.idea.phpClean.constrains.ConstrainInterface<Method> {
    override fun match(target: Method) = target.modifier.isStatic
}