package com.funivan.idea.phpClean.spl

import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer

class Pointer(private val keyword: PsiElement) {
    fun create(): SmartPsiElementPointer<PsiElement> {
        return SmartPointerManager.getInstance(keyword.project).createSmartPsiElementPointer(keyword)
    }
}