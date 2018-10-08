package com.funivan.idea.phpClean.constrains


class OneOf<T>(vararg private val constrains: ConstrainInterface<T>) : ConstrainInterface<T> {
    override fun match(target: T): Boolean {
        return constrains.any { it.match(target) }
    }
}