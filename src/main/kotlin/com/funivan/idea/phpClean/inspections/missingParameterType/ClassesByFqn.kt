package com.funivan.idea.phpClean.inspections.missingParameterType

import com.intellij.openapi.project.Project
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpClass

class ClassesByFqn(private val project: Project, private val fqns: Iterable<String>) {
    fun all(): List<PhpClass> {
        val index = PhpIndex.getInstance(project)
        return fqns.flatMap { index.getClassesByFQN(it) }.toList()
    }
}