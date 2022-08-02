package com.funivan.idea.phpClean.inspections.deprecatedDocTag

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class DeprecatedDocTagInspectionTest : BaseInspectionTest() {

    @Test
    fun testDeprecatedTag() {
        val inspection = DeprecatedDocTagInspection()
        inspection.tags.add("property")
        assert(
            inspection,
            """<?php
                    # Some comment
                    /**
                     * <warning descr="Deprecated tag">@property ${'$'}name</warning>
                     */
                     class User{}
                """,
            """<?php
                    # Some comment
class User{}
                """
        )
    }

    @Test
    fun testNegativeDeprecation() {
        val inspection = DeprecatedDocTagInspection()
        inspection.tags.addAll(listOf("property-read", "method"))
        assert(
            inspection,
            """<?php
                    /**
                     * @property ${'$'}user
                     * @property-write ${'$'}id
                     * <warning descr="Deprecated tag">@property-read ${'$'}name</warning>
                     * <warning descr="Deprecated tag">@method user()</warning>
                     */
                """,
            """<?php
                    /**
                     * @property ${'$'}user
                     * @property-write ${'$'}id
                     */
                """
        )
    }
}
