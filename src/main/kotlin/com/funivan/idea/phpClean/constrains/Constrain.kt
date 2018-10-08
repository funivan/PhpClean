package com.funivan.idea.phpClean.constrains


class Constrain<T>(private val constrain: (T) -> Boolean) : ConstrainInterface<T> {
    override fun match(target: T): Boolean {
        return constrain(target)
    }
}