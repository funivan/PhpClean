package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class ToStringCallInspectionTestTypeCast : BaseInspectionTest() {
    @Test
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

    @Test
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
