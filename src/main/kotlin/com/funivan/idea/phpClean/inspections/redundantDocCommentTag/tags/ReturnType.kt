package com.funivan.idea.phpClean.inspections.redundantDocCommentTag.tags

import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.resolve.types.PhpType

class ReturnType(private val returnTag: PhpDocReturnTag?, private val function: Function) : ParameterType {
    override fun doc(): PhpDocTag? {
        return returnTag
    }

    override fun type(): PhpType? {
        return function.declaredType
    }
}
