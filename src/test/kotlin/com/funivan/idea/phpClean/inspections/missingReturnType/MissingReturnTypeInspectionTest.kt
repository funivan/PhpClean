package com.funivan.idea.phpClean.inspections.missingReturnType

import com.funivan.idea.phpClean.BaseInspectionTest

class MissingReturnTypeInspectionTest : BaseInspectionTest() {

    fun testMethodWithoutReturnType() {
        assert(
                MissingReturnTypeInspection(),
                """<?php
                class Action {
                 /** @return void */
                  protected function <warning descr="Missing return type">hide</warning>(){}
                }
                """
        )
    }
    fun testWithoutReturnType() {
        assert(
                MissingReturnTypeInspection(),
                """<?php
                class Action {
                  protected function hide() : void {}
                }
                """
        )
    }

    fun testMagicMethods() {
        assert(
                MissingReturnTypeInspection(),
                """<?php
                class UserName {
                  protected function __construct(){}
                  protected function __clone(){}
                  protected function __destruct() {}
                }
                """
        )
    }
}