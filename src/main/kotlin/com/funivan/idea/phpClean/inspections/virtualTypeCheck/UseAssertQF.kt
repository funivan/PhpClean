package com.funivan.idea.phpClean.inspections.virtualTypeCheck

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocVariable
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Statement


class UseAssertQF(
        private val pointer: SmartPsiElementPointer<PsiElement>,
        private val variable: SmartPsiElementPointer<PhpDocVariable>,
        private val type: SmartPsiElementPointer<PhpDocType>
) : LocalQuickFix {
    override fun getFamilyName() = "Use assert"
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val tag = pointer.element
        val variableEl = variable.element
        val typeEl = type.element
        if (tag is PsiElement && variableEl is PhpDocVariable && typeEl is PhpDocType) {
            val new = PhpPsiElementFactory.createFromText(
                    project,
                    Statement::class.java,
                    "assert(${variableEl.text} instanceof ${typeEl.text});"
            )
            new?.let {
                tag.replace(it)
            }
        }
    }
}
