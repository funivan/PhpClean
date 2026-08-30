package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest
import org.jdom.Element
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * "Ignore classes with __toString" option. Off by default; when on, casting a value
 * whose class provides __toString() is not reported.
 */
class ToStringCallInspectionTestIgnoreToString : BaseInspectionTest() {

    private fun inspection(ignore: Boolean) = ToStringCallInspection().also {
        it.ignoreClassesWithToString = ignore
    }

    @Test
    fun testOptionIsDisabledByDefault() {
        assert(
            ToStringCallInspection(),
            """<?php
class Hello {
    public function __toString(): string { return 'Hi'; }
}
${'$'}phrase = new Hello();
(string)<warning descr="Deprecated __toString call">${'$'}phrase</warning>;
"""
        )
    }

    @Test
    fun testVariableIgnoredWhenClassHasToString() {
        assert(
            inspection(true),
            """<?php
class Hello {
    public function __toString(): string { return 'Hi'; }
}
${'$'}phrase = new Hello();
(string)${'$'}phrase;
echo ${'$'}phrase;
"""
        )
    }

    @Test
    fun testNewExpressionIgnoredWhenClassHasToString() {
        assert(
            inspection(true),
            """<?php
class Hello {
    public function __toString(): string { return 'Hi'; }
}
(string)new Hello();
"""
        )
    }

    @Test
    fun testMethodCallIgnoredWhenReturnedClassHasToString() {
        assert(
            inspection(true),
            """<?php
class BlaFoo {
    public function __toString(): string { return 'BlaFoo'; }
}
class Another {
    public function returnsBlaFoo(): BlaFoo { return new BlaFoo(); }
}
${'$'}another = new Another();
(string)${'$'}another->returnsBlaFoo();
(string)(${'$'}another->returnsBlaFoo());
"""
        )
    }

    @Test
    fun testStillReportedWhenClassHasNoToString() {
        assert(
            inspection(true),
            """<?php
class NoCast {
}
class Another {
    public function returnsNoCast(): NoCast { return new NoCast(); }
}
${'$'}another = new Another();
${'$'}plain = new NoCast();
(string)<warning descr="Deprecated __toString call">${'$'}plain</warning>;
(string)<warning descr="Deprecated __toString call">${'$'}another->returnsNoCast()</warning>;
"""
        )
    }

    @Test
    fun testOptionSurvivesProfileRoundTrip() {
        val saved = ToStringCallInspection()
        saved.ignoreClassesWithToString = true
        val element = Element("inspection")
        saved.writeSettings(element)

        val restored = ToStringCallInspection()
        assertFalse(restored.ignoreClassesWithToString)
        restored.readSettings(element)
        assertTrue(restored.ignoreClassesWithToString, "option must be persisted in the inspection profile")
    }

    @Test
    fun testInheritedToStringIsIgnored() {
        assert(
            inspection(true),
            """<?php
class Base {
    public function __toString(): string { return 'base'; }
}
class Child extends Base {
}
${'$'}child = new Child();
(string)${'$'}child;
"""
        )
    }
}
