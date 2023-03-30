package com.funivan.idea.phpClean.actions.useNamedConstructor

import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.RefactoringActionHandler
import com.intellij.refactoring.actions.BaseRefactoringAction
import com.jetbrains.php.lang.PhpLanguage
import com.jetbrains.php.lang.psi.elements.Method

class UseNamedConstructorAction : BaseRefactoringAction() {

    override fun isAvailableInEditorOnly() = false

    override fun isEnabledOnElements(elements: Array<out PsiElement>): Boolean {
        return elements.map(::isNamedConstructorCandidate).all { it == false }
    }

    override fun isAvailableOnElementInEditorAndFile(
        element: PsiElement,
        editor: Editor,
        file: PsiFile,
        context: DataContext
    ): Boolean {
        return isNamedConstructorCandidate(element)
    }

    private fun isNamedConstructorCandidate(element: PsiElement): Boolean {
        if (element is Method && element.isStatic) {
            val constructorParameters = element.containingClass?.constructor?.let { it.parameters.map { it.typeDeclaration?.text ?: "" } }
            val methodParameters = element.parameters.map { it.typeDeclaration?.text ?: "" }
            return constructorParameters == methodParameters
        }
        return false
    }

    override fun getHandler(dataContext: DataContext): RefactoringActionHandler {
        return UseNamedConstructorHandler()
    }

    override fun isAvailableForFile(file: PsiFile): Boolean {
        return isAvailableForLanguage(file.language)
    }

    override fun isAvailableForLanguage(language: Language): Boolean {
        return language.isKindOf(PhpLanguage.INSTANCE)
    }
}
