package com.funivan.idea.phpClean.inspections.architecture.returnType

import com.funivan.idea.phpClean.BaseInspectionTest

class MissingReturnTypeInspectionTest : BaseInspectionTest() {

    fun testProtectedMethod() {
        assert(
                MissingReturnTypeInspection(),
                """<?php
                class Action {
                 /**
                  * @return void
                  */
                  protected function <warning descr="Missing return type">hide</warning>(){}
                  public function show() : void {}
                }
                """
        )
    }
}