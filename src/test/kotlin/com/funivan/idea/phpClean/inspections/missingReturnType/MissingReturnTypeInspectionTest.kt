package com.funivan.idea.phpClean.inspections.missingReturnType

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class MissingReturnTypeInspectionTest : BaseInspectionTest() {

    @Test
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

    @Test
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

    @Test
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
