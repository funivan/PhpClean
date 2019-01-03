package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpTypedElement


class IsSingleClassType : ConstrainInterface<PhpTypedElement> {
    override fun match(target: PhpTypedElement): Boolean {
        var result = false
        val type = target.type
        if (type.types.size == 1 && type.isComplete) {
            result = PhpIndex.getInstance(target.project).getClassesByFQN(type.toStringResolved()).isNotEmpty()
        }
        return result
    }
}