package com.funivan.idea.phpClean.actions.useNamedConstructor

import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.RefactoringActionHandler
import com.jetbrains.php.lang.psi.elements.Method

class UseNamedConstructorHandler : RefactoringActionHandler {
    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext?) {
        if (editor == null) {
            return
        }
        dataContext
            ?.let { CommonDataKeys.PSI_ELEMENT.getData(it) as Method? }
            ?.let {
                val constructor = it.containingClass?.ownConstructor
                if (constructor != null) {
                    UseNamedConstructorProcessor(constructor, it).run()
                }
            }
    }

    override fun invoke(project: Project, elements: Array<PsiElement>, dataContext: DataContext?) {
        // Do nothing. This action can not be triggered in non-editor context
    }
}
