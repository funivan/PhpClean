package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class ToStringCallInspectionTest : BaseInspectionTest() {
    @Test
    fun testMethodCall() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                    class Hello {
                      public function randomize(): self { /* .. */return ${'$'}this; }
                      public function __toString(){ return 'Hi'; }
                    }
                    echo <warning descr="Deprecated __toString call">(new Hello())->randomize()</warning>;
                    """,
            """
                    <?php
                    class Hello {
                      public function randomize(): self { /* .. */return ${'$'}this; }
                      public function __toString(){ return 'Hi'; }
                    }
                    echo (new Hello())->randomize()->__toString();
                    """
        )
    }

    @Test
    fun testConcatenation() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                    class Hello {
                      public function randomize(): self { /* .. */return ${'$'}this; }
                      public function __toString(){ echo 'Hi'; }
                    }
                     ${'$'}phrase = 'Hi ' . <warning descr="Deprecated __toString call">(new Hello())->randomize()</warning>;
                    """,
            """
                    <?php
                    class Hello {
                      public function randomize(): self { /* .. */return ${'$'}this; }
                      public function __toString(){ echo 'Hi'; }
                    }
                     ${'$'}phrase = 'Hi ' . (new Hello())->randomize()->__toString();
                    """
        )
    }

    @Test
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
                     'Who say ' . (new Hello())->randomize()->__toString() . '?';
                    """
        )
    }

    @Test
    fun testNullableString() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                     class A{
                      function test(): ?string{ return null; }
                     }
                     echo (new A())->test();
                    """
        )
    }

    @Test
    fun testNewObject() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                    class Hello {
                      public function __toString(){ echo 'Hi'; }
                    }
                    echo <warning descr="Deprecated __toString call">new Hello()</warning>;
                    ${'$'}phrase = <warning descr="Deprecated __toString call">new Hello()</warning> . ' there';
                    """,
            """
                    <?php
                    class Hello {
                      public function __toString(){ echo 'Hi'; }
                    }
                    echo (new Hello())->__toString();
                    ${'$'}phrase = (new Hello())->__toString() . ' there';
                    """
        )
    }

    @Test
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
                    ${'$'}message = ${'$'}hi->str() . 'msg';
                    echo (new Hello())->s();
                    """
        )
    }

    @Test
    fun testStaticMethod() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                    class someClass {
                     public function __toString(){
                     }
                     public static function create() : self{
                     }
                    }
                    echo <warning descr="Deprecated __toString call">SomeClass::create()</warning>;
                    """,
            """
                    <?php
                    class someClass {
                     public function __toString(){
                     }
                     public static function create() : self{
                     }
                    }
                    echo SomeClass::create()->__toString();
                    """
        )
    }
}
