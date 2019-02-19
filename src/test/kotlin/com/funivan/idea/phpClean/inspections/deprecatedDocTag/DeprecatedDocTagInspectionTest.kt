package com.funivan.idea.phpClean.inspections.deprecatedDocTag

import com.funivan.idea.phpClean.BaseInspectionTest

class DeprecatedDocTagInspectionTest : BaseInspectionTest() {

    fun testDeprecatedTag() {
        assert(
                DeprecatedDocTagInspection(),
                """<?php
                    /**
                     * @property ${'$'}name
                     */
                     class User{}
                """
        )
    }
}