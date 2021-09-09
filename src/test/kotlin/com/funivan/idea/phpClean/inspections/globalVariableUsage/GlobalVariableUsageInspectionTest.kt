package com.funivan.idea.phpClean.inspections.globalVariableUsage

import com.funivan.idea.phpClean.BaseInspectionTest

class GlobalVariableUsageInspectionTest : BaseInspectionTest() {

    fun testGlobalVariableUsage() {
        assert(
                GlobalVariableUsageInspection(),
                """<?php
                    echo <warning descr="Global variable usage">${'$'}_GET</warning>['name'];
                """
        )
    }
}
