package com.funivan.idea.phpClean.inspections.methodShouldBeFinal

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class MethodShouldBeFinalInspectionTest : BaseInspectionTest() {

    @Test
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


    @Test
    fun testPhp81() {
        assert(
                MethodShouldBeFinalInspection(),
                """<?php
                    class CustomerDTO
                    {
                        public function <warning descr="Method should be final">test</warning>() : int{
                            return 1;
                        }
                        public function __construct(
                            public string ${'$'}name, 
                            public string ${'$'}email, 
                            public DateTimeImmutable ${'$'}birth_date,
                        ) {}
                    }
                """
        )
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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
    @Test
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
