package com.funivan.idea.phpClean.inspections.methodShouldBeFinal

import com.funivan.idea.phpClean.BaseInspectionTest

class MethodShouldBeFinalInspectionTest : BaseInspectionTest() {

    fun testMethodShouldBeFinal() {
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                class User{
                 public function <warning descr="Method should be final">name</warning>() : string {
                  return "";
                 }
                }
                """
        )
    }

    fun testSkipFinalClass() {
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                final class A{
                 public function id() : string{ return ""; }
                }
                """
        )
    }

    fun testSkipInterface() {
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                interface Name{
                 public function short() : string;
                }
                """
        )
    }

    fun testSkipAbstractMethod() {
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                abstract class UID {
                  abstract public function print() : void;
                }
                """
        )
    }

    fun testSkipMagicMethods() {
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                class Dealer {
                  public function __construct(){}
                  public function __destruct(){}
                  public function __call(){}
                }
                """
        )
    }

    fun testSkipStaticMethod(){
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                class Dealer {
                  public static function show(){}
                }
                """
        )
    }
    fun testSkipPrivateMethods(){
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                class Dealer {
                  private function show(){}
                }
                """
        )
    }
}