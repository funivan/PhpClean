package com.funivan.idea.phpClean.inspections.virtualTypeCheck

import com.funivan.idea.phpClean.BaseInspectionTest

class VirtualTypeCheckInspectionTest : BaseInspectionTest() {
    fun testUseAssert() {
        assert(
                VirtualTypeCheckInspection(),
                """<?php
                class User{}
                /** @var ${'$'}user <warning descr="Use assert to check variable type">User</warning> */;
                """
        )
    }

    fun testIgnoreArray() {
        assert(
                VirtualTypeCheckInspection(),
                """<?php
                /** @var ${'$'}user[] user */;
                /** @var ${'$'}user stdClass[] */;
                """
        )
    }

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

    fun testUnionType() {
        assert(
                VirtualTypeCheckInspection(),
                """<?php
                /** @var ${'$'}user string|null */;
                /** @var ${'$'}user stdClass|int */;
                """
        )
    }
}