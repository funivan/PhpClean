package com.funivan.idea.phpClean.inspections.virtualTypeCheck

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class VirtualTypeCheckInspectionTest : BaseInspectionTest() {
    @Test
    fun testUseAssert() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
                class User{}
                /** @var ${'$'}user <warning descr="Use assert to check variable type">User</warning> */;
                assert(${'$'}user instanceof User); // Valid
                """
        )
    }

    @Test
    fun testUseAssertWithInterface() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
                interface User{}
                /** @var ${'$'}user <warning descr="Use assert to check variable type">User</warning> */;
                assert(${'$'}user instanceof User); // Valid
                """
        )
    }

    @Test
    fun testIgnoreArray() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
                /** @var ${'$'}user[] user */;
                /** @var ${'$'}user stdClass[] */;
                """
        )
    }

    @Test
    fun testSkipPrimitiveTypes() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
                /** @var ${'$'}user string */;
                /** @var ${'$'}user int */;
                /** @var ${'$'}user resource */;
                /** @var ${'$'}user iterable */;
                /** @var ${'$'}user array */;
                /** @var ${'$'}user mixed */;
                """
        )
    }

    @Test
    fun testUnionType() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
                /** @var ${'$'}user string|null */;
                /** @var ${'$'}user stdClass|int */;
                """
        )
    }

    @Test
    fun testFunctionParam() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
                class Options{}
                /** @param ${'$'}options Options */
                function show(${'$'}options){

                }
                """
        )
    }

    @Test
    fun testReverseFormat() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
/** @var <warning descr="Use assert to check variable type">Options</warning> ${'$'}options */
class Options{};
                """.trimIndent(),
            """<?php
assert(${'$'}options instanceof Options);
class Options{};
                """.trimIndent()
        )
    }

    @Test
    fun testGenericAssertion() {
        assert(
            VirtualTypeCheckInspection(),
            """<?php
                class ListOfUsers{}
                class User{}
                /** @var ${'$'}user \ListOfUsers<User> */;
                """
        )
    }

}
