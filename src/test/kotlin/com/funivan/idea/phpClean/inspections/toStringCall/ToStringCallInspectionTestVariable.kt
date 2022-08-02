package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class ToStringCallInspectionTestVariable : BaseInspectionTest() {
    @Test
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
                    ${'$'}phrase->__toString();
                    """
        )
    }

    @Test
    fun testShortTagEcho() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                    class Hello {
                      public function __toString(){ echo 'Hi'; }
                    }
                    ${'$'}phrase = new Hello();
                    ?>
                    <?= <warning descr="Deprecated __toString call">${'$'}phrase</warning> ?>
                    """
        )
    }

    @Test
    fun testVariableComparison() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                    class Hello {
                      public function __toString(){ echo 'Hi'; }
                    }
                    ${'$'}phrase = new Hello();
                    <warning descr="Deprecated __toString call">${'$'}phrase</warning> == 'Hi';
                    "${'$'}a Hi" == <warning descr="Deprecated __toString call">${'$'}phrase</warning>;
                     ${'$'}phrase === 'test';
                     ${'$'}phrase == 123;
                    """
        )
    }
}
