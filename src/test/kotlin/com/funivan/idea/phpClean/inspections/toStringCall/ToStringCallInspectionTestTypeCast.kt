package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest

class ToStringCallInspectionTestTypeCast : BaseInspectionTest() {
    fun testMethodTypeCast() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    class Hello {
                      public function randomize(): self { /* .. */return ${'$'}this; }
                      public function __toString(){ echo 'Hi'; }
                    }
                     (string) <warning descr="Deprecated __toString call">(new Hello())->randomize()</warning>;
                     (string) (new Hello())->randomize()->__toString();
                    """
        )
    }

    fun testVariableTypeCast() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    ${'$'}a = "";
                     echo (string) ${'$'}a;
                    """
        )
    }
}