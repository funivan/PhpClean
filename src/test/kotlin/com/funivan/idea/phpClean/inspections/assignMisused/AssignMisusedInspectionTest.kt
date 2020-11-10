package com.funivan.idea.phpClean.inspections.assignMisused

import com.funivan.idea.phpClean.BaseInspectionTest

class AssignMisusedInspectionTest : BaseInspectionTest() {

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
