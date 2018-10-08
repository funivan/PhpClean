package com.funivan.idea.phpClean.constrains.method

import com.jetbrains.php.lang.psi.elements.Method


class Name(private val regex: Regex) : com.funivan.idea.phpClean.constrains.ConstrainInterface<Method> {

    override fun match(target: Method): Boolean {
        return regex.matches(target.name)
    }
}