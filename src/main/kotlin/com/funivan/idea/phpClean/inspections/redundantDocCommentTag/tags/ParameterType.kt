package com.funivan.idea.phpClean.inspections.redundantDocCommentTag.tags

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.psi.resolve.types.PhpType

interface ParameterType {
    fun doc() : PhpDocTag?
    fun type() : PhpType?
}