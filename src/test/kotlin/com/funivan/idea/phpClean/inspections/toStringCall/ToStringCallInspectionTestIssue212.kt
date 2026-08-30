package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

/**
 * Snippet from issue #212.
 *
 * `(string)$another->returnsBlaFoo()` casts the RETURNED BlaFoo, not `$another`,
 * so the report belongs on the method call. `((string)$another)->returnsBlaFoo()`
 * is a different expression and is reported on the variable.
 */
class ToStringCallInspectionTestIssue212 : BaseInspectionTest() {
    @Test
    fun testIssue212Snippet() {
        assert(
            ToStringCallInspection(),
            """<?php
declare(strict_types=1);

class BlaFoo
{
    public function __toString(): string
    {
        return 'BlaFoo';
    }
}

class Another {
    public function returnsBlaFoo(): BlaFoo {
        return new BlaFoo();
    }

    public function __toString(): string
    {
        return '';
    }
}

${'$'}another = new Another();

(string)(<warning descr="Deprecated __toString call">${'$'}another->returnsBlaFoo()</warning>);

(string)<warning descr="Deprecated __toString call">${'$'}another->returnsBlaFoo()</warning>;

((string)<warning descr="Deprecated __toString call">${'$'}another</warning>)->returnsBlaFoo();
"""
        )
    }
}
