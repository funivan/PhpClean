package com.funivan.idea.phpClean.inspections.methodCanBePrivate

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.SmartPointerManager
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpModifierList
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor


class MethodCanBePrivateInspection : PhpInspection() {
    override fun getShortName() = "MethodCanBePrivateInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(clazz: PhpClass) {
                if (clazz.isFinal && clazz.extendsList.referenceElements.isEmpty()) {
                    for (method in clazz.ownMethods.filter { it.modifier.isProtected }) {
                        val name = method.nameIdentifier
                        var target: PsiElement = method
                        if (name != null) {
                            target = name
                        }
                        val firstChild = method.firstChild
                        var qf: LocalQuickFix? = null
                        if (firstChild is PhpModifierList) {
                            val keyword = firstChild.getNode().findChildByType(PhpTokenTypes.kwPROTECTED)?.psi
                            if (keyword !== null) {
                                qf = MakeMethodPrivateQF(
                                        SmartPointerManager.getInstance(keyword.project).createSmartPsiElementPointer(keyword)
                                )
                            }
                        }
                        holder.registerProblem(target, "Method can be private", qf)
                    }
                }
            }
        }
    }
}