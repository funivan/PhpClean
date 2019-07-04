package com.funivan.idea.phpClean.constrains.method

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.jetbrains.php.lang.psi.elements.Method

class IsMagic : ConstrainInterface<Method> {
    companion object {
        val names = setOf(
                "__construct",
                "__destruct",
                "__call",
                "__callstatic",
                "__get",
                "__set",
                "__isset",
                "__unset",
                "__sleep",
                "__wakeup",
                "__tostring",
                "__invoke",
                "__set_state",
                "__clone",
                "__debuginfo"
        )
    }

    override fun match(target: Method): Boolean {
        return names.contains(target.name.toLowerCase())
    }
}