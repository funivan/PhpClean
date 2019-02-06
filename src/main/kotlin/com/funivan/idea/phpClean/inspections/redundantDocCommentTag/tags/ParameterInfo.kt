package com.funivan.idea.phpClean.inspections.redundantDocCommentTag.tags

import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.resolve.types.PhpType

class ParameterInfo(private val paramTag: PhpDocParamTag, private val function: Function) : ParameterType {
    override fun doc(): PhpDocTag? {
        return paramTag
    }

    override fun type(): PhpType? {
        return function.parameters
                .filter { it.name == paramTag.varName }
                .map { it.declaredType }
                .firstOrNull()
    }

}