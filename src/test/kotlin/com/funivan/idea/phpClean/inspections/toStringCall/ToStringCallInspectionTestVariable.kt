package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest

class ToStringCallInspectionTestVariable : BaseInspectionTest() {
    fun testVariableEchoContext() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    class Hello {
                      public function __toString(){ echo 'Hi'; }
                    }
                    ${'$'}phrase = new Hello();
                    echo <warning descr="Deprecated __toString call">${'$'}phrase</warning>;
                    echo ${'$'}phrase->__toString();
                    """
        )
    }
}