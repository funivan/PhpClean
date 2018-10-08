package com.funivan.idea.phpClean.constrains.method.aliases

import com.jetbrains.php.lang.psi.elements.Method


class Public : com.funivan.idea.phpClean.constrains.ConstrainInterface<Method> {
    override fun match(target: Method) = target.modifier.isPublic
}