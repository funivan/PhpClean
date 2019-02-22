package com.funivan.idea.phpClean.spl.ci

import com.funivan.idea.phpClean.inspections.deprecatedDocTag.DeprecatedDocTagInspection
import com.funivan.idea.phpClean.inspections.globalVariableUsage.GlobalVariableUsageInspection
import com.funivan.idea.phpClean.inspections.methodCanBePrivate.MethodCanBePrivateInspection
import com.funivan.idea.phpClean.inspections.methodShouldBeFinal.MethodShouldBeFinalInspection
import com.funivan.idea.phpClean.inspections.methodVisibility.MethodVisibilityInspection
import com.funivan.idea.phpClean.inspections.missingParameterType.MissingParameterTypeDeclarationInspection
import com.funivan.idea.phpClean.inspections.missingReturnType.MissingReturnTypeInspection
import com.funivan.idea.phpClean.inspections.phpCleanUndefinedMethod.PhpCleanUndefinedMethodInspection
import com.funivan.idea.phpClean.inspections.propertyAnnotation.PropertyAnnotationInspection
import com.funivan.idea.phpClean.inspections.redundantDocCommentTag.RedundantDocCommentTagInspection
import com.funivan.idea.phpClean.inspections.toStringCall.ToStringCallInspection
import com.funivan.idea.phpClean.inspections.virtualTypeCheck.VirtualTypeCheckInspection


class AllInspections {
    fun all() = listOf(
            DeprecatedDocTagInspection(),
            GlobalVariableUsageInspection(),
            MethodCanBePrivateInspection(),
            MethodShouldBeFinalInspection(),
            MethodVisibilityInspection(),
            MissingParameterTypeDeclarationInspection(),
            MissingReturnTypeInspection(),
            PhpCleanUndefinedMethodInspection(),
            PropertyAnnotationInspection(),
            RedundantDocCommentTagInspection(),
            ToStringCallInspection(),
            VirtualTypeCheckInspection()
    )
}