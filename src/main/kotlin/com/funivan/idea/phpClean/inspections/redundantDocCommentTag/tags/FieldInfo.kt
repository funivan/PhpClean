package com.funivan.idea.phpClean.inspections.redundantDocCommentTag.tags

import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.resolve.types.PhpType

class FieldInfo(private val paramTag: PhpDocParamTag, private val field: Field) : ParameterType {
    override fun doc(): PhpDocTag? {
        return paramTag
    }

    override fun type(): PhpType? {
        return field.declaredType
    }
}