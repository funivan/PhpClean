package com.funivan.idea.phpClean.constrains

class NoneOf<T>(vararg private val constrains: ConstrainInterface<T>) : ConstrainInterface<T> {
    override fun match(target: T): Boolean {
        return constrains.none { it.match(target) }
    }
}