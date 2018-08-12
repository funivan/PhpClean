package com.funivan.idea.phpTypes.typeInspection.instruction

import com.intellij.openapi.util.TextRange
import com.jetbrains.php.lang.psi.resolve.types.PhpType


data class Instruction(val name: String, val type: PhpType, val range: TextRange) : InstructionInterface {
    override fun name(): String {
        return name
    }

    override fun range(): TextRange {
        return range
    }

    override fun type(): PhpType {
        return type
    }


}