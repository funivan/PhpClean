package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest

class ToStringCallInspectionTestFunction : BaseInspectionTest() {
    fun testFunctionCall() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    class Phrase {
                      public function __toString(){ echo 'Hello'; }
                    }
                    function hi() : Phrase {
                      return new Phrase();
                    }
                    echo <warning descr="Deprecated __toString call">hi()</warning>;
                    echo hi()->__toString();
                    """
        )
    }
}