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
                    hi();
                    hi()->__toString();
                    echo getcwd() . "/test.php";
                    """
        )
    }
}
