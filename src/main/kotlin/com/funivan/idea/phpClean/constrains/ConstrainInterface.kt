package com.funivan.idea.phpClean.constrains


interface ConstrainInterface<T> {
    fun match(target: T): Boolean
    infix fun and(next: ConstrainInterface<T>): ConstrainInterface<T> {
        return AllOf(this, next)
    }

}