package com.funivan.idea.phpClean.constrains


interface ConstrainInterface<T> {
    fun match(target: T): Boolean
    operator fun invoke(target: T): Boolean {
        return match(target)
    }
}