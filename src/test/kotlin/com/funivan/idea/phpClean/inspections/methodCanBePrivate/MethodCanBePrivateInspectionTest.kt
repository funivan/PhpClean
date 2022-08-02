package com.funivan.idea.phpClean.inspections.methodCanBePrivate

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class MethodCanBePrivateInspectionTest : BaseInspectionTest() {

    @Test
    fun testFindMethodsThatCanBePrivate() {
        assert(
            MethodCanBePrivateInspection(),
            """<?php
                final class User {
                  protected function <warning descr="Method can be private">name</warning>() {}
                }
                """,
            """<?php
                final class User {
                  private function name() {}
                }
                """
        )
    }

    @Test
    fun testFindMethodsInNonFinalClass() {
        assert(
            MethodCanBePrivateInspection(),
            """<?php
                class A {
                  protected function name() {}
                }
                """
        )
    }

    @Test
    fun testFindMethodsInClassWithExtends() {
        assert(
            MethodCanBePrivateInspection(),
            """<?php
                class Test{}
                final class Uid extends Test {
                  protected function id() {}
                }
                """
        )
    }

    @Test
    fun testCheckPublicMethod() {
        assert(
            MethodCanBePrivateInspection(),
            """<?php
                final class Id {
                  public function __construct() {}
                  public function size() {}
                }
                """
        )
    }

}
