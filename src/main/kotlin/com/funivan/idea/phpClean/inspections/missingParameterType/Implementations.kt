package com.funivan.idea.phpClean.inspections.missingParameterType

import com.jetbrains.php.lang.psi.elements.PhpClass

class Implementations(val phpClass: PhpClass) : Iterable<String> {
    override fun iterator(): Iterator<String> {
        return phpClass.extendsList.referenceElements
                .plus(phpClass.implementsList.referenceElements)
                .map { it.fqn }
                .filterNotNull()
                .iterator()
    }
}