package com.funivan.idea.phpClean.constrains.clazz

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.jetbrains.php.PhpClassHierarchyUtils
import com.jetbrains.php.lang.psi.elements.PhpClass

class IsAloneClass : ConstrainInterface<PhpClass> {
    override fun match(target: PhpClass): Boolean {
        return (
                !target.isAbstract &&
                        PhpClassHierarchyUtils.getAllSubclasses(target).isEmpty()
                        &&
                        target.extendsList.referenceElements.isEmpty())
    }

}
