package com.funivan.idea.phpClean.intentions.unimportClass

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jetbrains.php.PhpWorkaroundUtil
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.ClassReference
import com.jetbrains.php.lang.psi.elements.PhpPsiElement
import com.jetbrains.php.lang.psi.elements.PhpUse
import com.jetbrains.php.lang.psi.visitors.PhpRecursiveElementVisitor

class UnimportClassIntention : PsiElementBaseIntentionAction() {


    override fun getText() = "Unimport class"
    override fun getFamilyName() = text

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {

        if (!PhpWorkaroundUtil.isIntentionAvailable(element) || element.parent == null) {
            return false
        }

        val baseElement = element.parent as? ClassReference ?: return false

        val fqn = baseElement.fqn
        return fqn != baseElement.text || baseElement.parent is PhpUse
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        var element = element
        element = element.parent
        val classReference = element as ClassReference

        val scopeForUseOperator = PhpCodeInsightUtil.findScopeForUseOperator(classReference)!!

        val fqn = classReference.fqn
        val newClassRef = PhpPsiElementFactory.createClassReference(project, fqn!!)

        scopeForUseOperator.acceptChildren(object : PhpRecursiveElementVisitor() {
            override fun visitPhpElement(element: PhpPsiElement) {
                if (!PhpCodeInsightUtil.isScopeForUseOperator(element)) {
                    super.visitPhpElement(element)
                }
            }

            override fun visitPhpDocType(phpDocType: PhpDocType) {
                if (!phpDocType.isAbsolute && phpDocType.text == classReference.text) {
                    // probably idea byg. FQN of docElement consist of Current namespace + ClassName
                    replace(phpDocType)
                }
            }

            override fun visitPhpClassReference(classReference: ClassReference) {
                if (classReference.parent !is PhpUse && classReference.fqn == fqn) {
                    replace(classReference)
                }
            }

            private fun replace(currentElement: PsiElement) {
                if (currentElement.text != newClassRef.text) {
                    currentElement.replace(newClassRef)
                }
            }
        })
    }
}
