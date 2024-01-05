package com.funivan.idea.phpClean.actions.useNamedConstructor

import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentOfTypes
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.usageView.UsageInfo
import com.intellij.usageView.UsageViewDescriptor
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.NewExpression

class UseNamedConstructorProcessor(
    val constructor: Method,
    val namedConstructor: Method,
) : BaseRefactoringProcessor(constructor.project) {

    override fun createUsageViewDescriptor(usages: Array<out UsageInfo>): UsageViewDescriptor {
        return NamedConstructorUsageViewDescriptor(constructor, namedConstructor)
    }

    override fun findUsages(): Array<UsageInfo> {
        return ReferencesSearch.search(constructor)
            .map { it.element.parent as? NewExpression }
            .filter {
                val inMethod = it?.parentOfTypes(Method::class)
                if (inMethod is Method) {
                    val isSameM = inMethod.name == namedConstructor.name
                    val isSameClass = inMethod.containingClass?.fqn == namedConstructor.containingClass?.fqn
                    (isSameM && isSameClass) == false
                } else {
                    true
                }
            }
            .filterNotNull()
            .map { UsageInfo(it) }.toTypedArray()
    }

    override fun performRefactoring(usages: Array<UsageInfo>) {
        usages.forEach {
            (it.element as? NewExpression)?.let {
                val n = it.classReference?.element?.text
                if (n != null) {
                    it.replace(
                        PhpPsiElementFactory.createMethodReference(
                            it.project,
                            n + "::" + namedConstructor.name
                                    + "(" + it.parameterList?.text.orEmpty() + ")"
                        )
                    )
                }
            }
        }
    }

    override fun getCommandName() = "Use named constructor"
}
