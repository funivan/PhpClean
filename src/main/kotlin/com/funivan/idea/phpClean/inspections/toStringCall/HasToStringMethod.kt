package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.constrains.ConstrainInterface
import com.intellij.openapi.project.Project
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.resolve.types.PhpType


/**
 * Matches when the type resolves to at least one class and every resolved class provides __toString().
 */
class HasToStringMethod(private val project: Project) : ConstrainInterface<PhpType> {
    override fun match(target: PhpType): Boolean {
        val index = PhpIndex.getInstance(project)
        val classes = target.global(project).types
                .filterNot { PhpType.isPrimitiveType(it) }
                .flatMap { index.getAnyByFQN(it) }
        return classes.isNotEmpty() && classes.all { it.findMethodByName("__toString") != null }
    }
}
