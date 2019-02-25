package com.funivan.idea.phpClean.inspections.methodCanBePrivate

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.spl.Pointer
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpModifierList
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class MethodCanBePrivateInspection : PhpCleanInspection() {
    override fun getShortName() = "MethodCanBePrivateInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(clazz: PhpClass) {
                if (clazz.isFinal && clazz.extendsList.referenceElements.isEmpty()) {
                    for (method in clazz.ownMethods.filter { it.modifier.isProtected }) {
                        holder.registerProblem(
                                method.nameIdentifier ?: method,
                                "Method can be private",
                                quickFix(method)
                        )
                    }
                }
            }
        }
    }

    private fun quickFix(method: Method): LocalQuickFix? {
        return protectedKeyword(method)?.let {
            MakeMethodPrivateQF(
                    Pointer(it).create()
            )
        }
    }

    private fun protectedKeyword(method: Method): PsiElement? {
        var keyword: PsiElement? = null
        val firstChild = method.firstChild
        if (firstChild is PhpModifierList) {
            keyword = firstChild.getNode().findChildByType(PhpTokenTypes.kwPROTECTED)?.psi
        }
        return keyword
    }
}