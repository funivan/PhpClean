package com.funivan.idea.phpClean.inspections.globalVariableUsage

import com.funivan.idea.phpClean.BaseInspectionTest
import com.funivan.idea.phpClean.inspections.methodCanBePrivate.MethodCanBePrivateInspection

class GlobalVariableUsageInspectionTest : BaseInspectionTest() {

    fun testGlobalVariableUsage() {
        assert(
                GlobalVariableUsageInspection(),
                """<?php
                    echo <warning descr="Deprecated global variable usage">${'$'}_GET</warning>['name'];
                """
        )
    }
}