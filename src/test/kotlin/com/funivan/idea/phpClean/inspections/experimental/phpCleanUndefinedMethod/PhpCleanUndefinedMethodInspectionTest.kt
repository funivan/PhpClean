package com.funivan.idea.phpClean.inspections.experimental.phpCleanUndefinedMethod

import com.funivan.idea.phpClean.BaseInspectionTest

class PhpCleanUndefinedMethodInspectionTest : BaseInspectionTest() {

    fun testInvalidMethod() {
        assert(
                PhpCleanUndefinedMethodInspection(),
                """<?php
                final class Email { public function send(){}}
                /** @var mixed|Email ${'$'}email */;
                ${'$'}email->send();
                ${'$'}email-><warning descr="Undefined method">show</warning>();
                """
        )
    }
    fun testArray() {
        assert(
                PhpCleanUndefinedMethodInspection(),
                """<?php
                class Name {
                    public function value(){}
                }
                class User {
                 /**
                  * @var Name[]
                  */
                  private ${'$'}names;
                  public function __construct(){
                    ${'$'}this->names = [
                      create Name(),
                      create Name(),
                    ];
                  }

                  public function display(){
                    foreach(${'$'}this->names as ${'$'}name){
                        ${'$'}name->value();
                        ${'$'}name-><warning descr="Undefined method">vle</warning>();

                    }
                  }

                }
                """
        )
    }

}