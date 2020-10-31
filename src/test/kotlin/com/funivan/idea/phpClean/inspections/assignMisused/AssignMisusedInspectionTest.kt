package com.funivan.idea.phpClean.inspections.assignMisused

import com.funivan.idea.phpClean.BaseInspectionTest

class AssignMisusedInspectionTest : BaseInspectionTest() {

    fun testSimpleCase() {
        assert(
                AssignMisusedInspection(),
                """<?php
                    ${'$'}b = 123;
                    ${'$'}c == ${'$'}b;
                    <warning descr="Assignment and comparison operators used in one statement">${'$'}a = ${'b'}===1</warning>;
                """
        )
    }
    fun testComparisonAndAssignment() {
        assert(
                AssignMisusedInspection(),
                """<?php
                    do{
                    ${'$'}c = ${'$'}b = ${'$'}f;
                    } while (<warning descr="Assignment and comparison operators used in one statement">false !== ${'$'}a = ${'$'}b</warning>);
                """
        )
    }
}
