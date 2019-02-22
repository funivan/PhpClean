package com.funivan.idea.phpClean.inspections.deprecatedDocTag

import com.funivan.idea.phpClean.BaseInspectionTest

class DeprecatedDocTagInspectionTest : BaseInspectionTest() {

    fun testDeprecatedTag() {
        val inspection = DeprecatedDocTagInspection()
        inspection.tags.add("property")
        assert(
                inspection,
                """<?php
                    /**
                     * <warning descr="Deprecated tag">@property ${'$'}name</warning>
                     */
                     class User{}
                """
        )
    }

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
                """
        )
    }
}