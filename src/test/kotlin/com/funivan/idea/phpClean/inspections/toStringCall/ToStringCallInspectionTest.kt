package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest

class ToStringCallInspectionTest : BaseInspectionTest() {
    fun testMethodCall() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    class Hello {
                      public function randomize(): self { /* .. */return ${'$'}this; }
                      public function __toString(){ echo 'Hi'; }
                    }
                     <warning descr="Deprecated __toString call">echo</warning>(new Hello())->randomize();
                    """
        )
    }

    fun testManualTypeCasting() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    class Hello {
                      public function randomize(): self { /* .. */return ${'$'}this; }
                      public function __toString(){ echo 'Hi'; }
                    }
                     echo (new Hello())->randomize()->__toString();
                    """
        )
    }

    fun testNewObject() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    class Hello {
                      public function __toString(){ echo 'Hi'; }
                    }
                    <warning descr="Deprecated __toString call">echo</warning> new Hello();
                    """
        )
    }
    fun testMethodStringReturned() {
        assert(
                ToStringCallInspection(),
                """
                    <?php
                    class Hello {
                      public function str():string{ return 'Hi'; }
                      /**
                       * @return string|int|boolean
                       */
                      public function s(){ echo 'Hi'; }
                    }
                    echo (new Hello())->str();
                    ${'$'}hi = new Hello();
                    echo ${'$'}hi->str();
                    echo (new Hello())->s();
                    """
        )
    }
}