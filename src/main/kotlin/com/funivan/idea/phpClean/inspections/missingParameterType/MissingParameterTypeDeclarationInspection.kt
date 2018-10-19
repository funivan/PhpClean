package com.funivan.idea.phpClean.inspections.missingParameterType


import com.funivan.idea.phpClean.constrains.Constrain
import com.funivan.idea.phpClean.visitors.ParameterVisitor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.inspections.PhpInspection


class MissingParameterTypeDeclarationInspection : PhpInspection() {

    override fun getShortName(): String {
        return "MissingParameterTypeDeclarationInspection"
    }


    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return ParameterVisitor(
                Constrain { it.declaredType.size() == 0 },
                "Missing parameter type",
                holder
        )
    }

}
