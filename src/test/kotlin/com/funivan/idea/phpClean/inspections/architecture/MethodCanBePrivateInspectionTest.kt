package com.funivan.idea.phpClean.inspections.architecture

import com.funivan.idea.phpClean.BaseInspectionTest

class MethodCanBePrivateInspectionTest : BaseInspectionTest() {

    fun testFindMethodsThatCanBePrivate() {
        assert(
                MethodCanBePrivateInspection(),
                """<?php
                final class A {
                  protected function <warning descr="Method can be private">name</warning>() {}
                }
                """,
                """<?php
                final class A {
                  private function name() {}
                }
                """
        )
    }

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

}