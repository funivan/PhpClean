package com.funivan.idea.phpClean.spl

import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer

class Pointer<T : PsiElement>(private val keyword: T) {
    fun create(): SmartPsiElementPointer<T> {
        return SmartPointerManager.getInstance(keyword.project).createSmartPsiElementPointer(keyword)
    }
}