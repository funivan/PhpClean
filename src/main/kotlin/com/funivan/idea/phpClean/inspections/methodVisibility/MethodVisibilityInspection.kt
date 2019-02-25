package com.funivan.idea.phpClean.inspections.methodVisibility

import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.funivan.idea.phpClean.visitors.MethodVisitor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor


class MethodVisibilityInspection : PhpCleanInspection() {

    override fun getShortName(): String {
        return "MethodVisibilityInspection"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return MethodVisitor(
                { it.modifier.isProtected },
                "Do not write protected methods. Only public or private",
                holder
        )
    }
}