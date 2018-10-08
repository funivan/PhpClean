package com.funivan.idea.phpClean.constrains

class Not<T>(private val constrain: ConstrainInterface<T>) : ConstrainInterface<T> {
    override fun match(target: T): Boolean {
        return !constrain.match(target)
    }
}