package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

/**
 * Redundant parentheses must not hide the implicit __toString call.
 * `(string)($o->m())` is the same expression as `(string)$o->m()`.
 */
class ToStringCallInspectionTestParenthesized : BaseInspectionTest() {

    @Test
    fun testParenthesizedMethodCall() {
        assert(
            ToStringCallInspection(),
            """<?php
class BlaFoo {
    public function __toString(): string { return 'BlaFoo'; }
}
class Another {
    public function returnsBlaFoo(): BlaFoo { return new BlaFoo(); }
}
${'$'}another = new Another();
(string)(<warning descr="Deprecated __toString call">${'$'}another->returnsBlaFoo()</warning>);
(string)<warning descr="Deprecated __toString call">${'$'}another->returnsBlaFoo()</warning>;
(string)((<warning descr="Deprecated __toString call">${'$'}another->returnsBlaFoo()</warning>));
"""
        )
    }

    @Test
    fun testParenthesizedVariable() {
        assert(
            ToStringCallInspection(),
            """<?php
class Hello {
    public function __toString(): string { return 'Hi'; }
}
${'$'}phrase = new Hello();
(string)(<warning descr="Deprecated __toString call">${'$'}phrase</warning>);
echo (<warning descr="Deprecated __toString call">${'$'}phrase</warning>);
"""
        )
    }

    @Test
    fun testParenthesizedScalarReturnStillClean() {
        assert(
            ToStringCallInspection(),
            """<?php
class Another {
    public function returnsString(): string { return 'x'; }
    public function __toString(): string { return ''; }
}
${'$'}another = new Another();
(string)(${'$'}another->returnsString());
"""
        )
    }
}
