package com.funivan.idea.phpClean.experimental.typeInspection.instruction

import com.intellij.openapi.util.TextRange
import com.jetbrains.php.lang.psi.resolve.types.PhpType


interface InstructionInterface {
    fun name(): String
    fun range(): TextRange
    fun type(): PhpType
}