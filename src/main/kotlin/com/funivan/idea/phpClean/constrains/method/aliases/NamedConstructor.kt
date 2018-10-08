package com.funivan.idea.phpClean.constrains.method.aliases

import com.funivan.idea.phpClean.constrains.method.Name
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass


class NamedConstructor : com.funivan.idea.phpClean.constrains.ConstrainInterface<Method> {
    private val constrains = Name(Regex("^create.*$"))
    override fun match(target: Method): Boolean {
        var result = false
        if (constrains.match(target)) {
            val containingClass = target.containingClass
            if (containingClass is PhpClass) {
                val returnType = target.returnType
                if (returnType != null) {
                    // @todo filter primitive (\int|\array|\...) types
                    val returnTypeFQN = returnType.classReference.fqn
                    result = recursiveInheritList(containingClass).any { it == returnTypeFQN }
                }
            }
        }
        return result
    }

    private fun recursiveInheritList(targetClass: PhpClass): Set<String> {
        val result = mutableSetOf(targetClass.fqn)
        for (referenceElement in targetClass.implementedInterfaces) {
            result.addAll(recursiveInheritList(referenceElement))
        }
        val parent = targetClass.superClass
        if (parent != null) {
            result.addAll(recursiveInheritList(parent))
        }
        return result
    }
}