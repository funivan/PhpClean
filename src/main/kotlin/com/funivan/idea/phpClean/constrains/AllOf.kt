package com.funivan.idea.phpClean.constrains


class AllOf<T>(vararg private val constrains: ConstrainInterface<T>) : ConstrainInterface<T> {
    override fun match(target: T): Boolean {
        return constrains.all { it.match(target) }
    }
}