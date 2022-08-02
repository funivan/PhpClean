package com.funivan.idea.phpClean.inspections.assignMisused

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class AssignMisusedInspectionTest : BaseInspectionTest() {

    @Test
    fun testValidCase() {
        assert(
            AssignMisusedInspection(),
            """<?php
                    ${'$'}b = 123;
                    ${'$'}c == ${'$'}b;
                    ${'$'}a = ${'b'}===1;
                """
        )
    }

    @Test
    fun testComparisonAndAssignment() {
        assert(
            AssignMisusedInspection(),
            """<?php
                    ${'$'}c = ${'$'}b = ${'$'}f;
                    do{} while (<warning descr="Assignment and comparison operators used in one statement">false !== ${'$'}a = ${'$'}b</warning>);
                    do{} while (<warning descr="Assignment and comparison operators used in one statement">false !== ${'$'}a = ${'$'}b</warning>);
                    do{} while (<warning descr="Assignment and comparison operators used in one statement">false == ${'$'}a = ${'$'}b</warning>);
                    do{} while (<warning descr="Assignment and comparison operators used in one statement">false != ${'$'}a = ${'$'}b</warning>);
                    if(<warning descr="Assignment and comparison operators used in one statement">0 > ${'$'}a = ${'$'}b</warning>){}
                    if(<warning descr="Assignment and comparison operators used in one statement">0 >= ${'$'}a = ${'$'}b</warning>){}
                    if(<warning descr="Assignment and comparison operators used in one statement">0 < ${'$'}a = ${'$'}b</warning>){}
                    if(<warning descr="Assignment and comparison operators used in one statement">0 <= ${'$'}a = ${'$'}b</warning>){}
                """
        )
    }

    @Test
    fun testMultiplyOperator() {
        assert(
            AssignMisusedInspection(),
            """<?php
                    ${'$'}i = ${'$'}j = ${'$'}f = 0;
                    ${'$'}f = ${'$'}j * ${'$'}f + ${'$'}i;
                    ${'$'}j = ${'$'}k !== ${'$'}l;
                """
        )
    }
}
