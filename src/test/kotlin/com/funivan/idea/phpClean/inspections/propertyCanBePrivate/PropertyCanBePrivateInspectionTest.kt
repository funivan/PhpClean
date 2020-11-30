package com.funivan.idea.phpClean.inspections.propertyCanBePrivate

import com.funivan.idea.phpClean.BaseInspectionTest

class PropertyCanBePrivateInspectionTest : BaseInspectionTest() {

    fun testCheckFinalClasses() {
        assert(
                PropertyCanBePrivateInspection(),
                """<?php
                final class User {
                  protected <warning descr="Property can be private">${'$'}user</warning>;
                }
                """,
                """<?php
                final class User {
                  private ${'$'}user;
                }
                """
        )
    }

    fun testPropertyInExtensionClass() {
        assert(
                PropertyCanBePrivateInspection(),
                """<?php
                class A2 {
                  protected ${'$'}name;
                }
                class B2 extends \A2 { }
                """
        )
    }
    fun testPropertyInExtendedClass() {
        assert(
                PropertyCanBePrivateInspection(),
                """<?php
                class A1{}
                class B1 extends A1 {
                  protected ${'$'}name;
                }
                """
        )
    }

    fun testCheckPublicProperties() {
        assert(
                PropertyCanBePrivateInspection(),
                """<?php
                final class Id {
                  public array ${'$'}user;
                }
                """
        )
    }

    fun testCheckWithTypeDefinition() {
        assert(
                PropertyCanBePrivateInspection(),
                """<?php
                class Id {
                  protected array <warning descr="Property can be private">${'$'}user</warning>;
                }
                """,
                """<?php
                class Id {
                  private array ${'$'}user;
                }
                """
        )
    }
    fun testCheckTrait() {
        assert(
                PropertyCanBePrivateInspection(),
                """<?php
                trait Number {
                  protected int ${'$'}val;
                }
                """
        )
    }

    fun testCheckAbstractClass() {
        assert(
                PropertyCanBePrivateInspection(),
                """<?php
                abstract class Str {
                  protected ${'$'}fast;
                }
                """
        )
    }

}
