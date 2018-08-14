package com.funivan.idea.phpClean.experimental.typeInspection

import com.funivan.idea.phpClean.experimental.typeInspection.instruction.Instruction
import com.funivan.idea.phpClean.experimental.typeInspection.instruction.InstructionInterface
import com.intellij.openapi.util.TextRange
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.If
import com.jetbrains.php.lang.psi.elements.ParameterList
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.resolve.types.PhpType


class TypeCheckActionsFinder(val ifConditions: MutableCollection<If>) {
    fun find(input: Variable): List<InstructionInterface> {
        val result = mutableListOf<InstructionInterface>()
        val parent = input.parent
        if (parent is ParameterList) {
            val fnCall = parent.parent
            // check if not string
            if (fnCall is FunctionReference && fnCall.fqn == "\\is_string") {
                //detect if fn call is in range of the if check
                val inIfScope: TextRange? = ifConditions.map { it.lastChild.textRange }.firstOrNull()
                if (inIfScope != null) {
                    result.add(
                            Instruction(
                                    input.name,
                                    PhpType.STRING,
                                    TextRange(fnCall.textRange.endOffset, inIfScope.endOffset)
                            )
                    )
                } else {
                    println("no if scope")
                }
            }
        }
        return result
    }
}