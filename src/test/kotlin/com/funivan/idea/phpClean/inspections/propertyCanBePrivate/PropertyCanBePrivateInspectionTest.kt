package com.funivan.idea.phpClean.inspections.propertyCanBePrivate

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class PropertyCanBePrivateInspectionTest : BaseInspectionTest() {

    @Test
    fun testPhp81() {
        assert(
            PropertyCanBePrivateInspection(),
            """<?php
                    final class UserDTO {
                        public function __construct(
                            protected string <warning descr="Property can be private">${'$'}name</warning> 
                        ) {}
                    }
                """,
            """<?php
                    final class UserDTO {
                        public function __construct(
                            private string ${'$'}name 
                        ) {}
                    }
                """
        )
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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
