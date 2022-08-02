package com.funivan.idea.phpClean.inspections.prohibitedClassExtend

import com.funivan.idea.phpClean.BaseInspectionTest
import com.funivan.idea.phpClean.inspections.classNameCollision.ProhibitedClassExtendInspection
import kotlin.test.Test

internal class ProhibitedClassExtendInspectionTest : BaseInspectionTest() {
    @Test
    fun testProhibitExtend() {
        assert(
            ProhibitedClassExtendInspection(),
            """<?php
                    /**
                     * @final
                     */
                    class User{}
                    class <warning descr="Prohibited extentions of @final class \User">Admin</warning> extends User{}
                """
        )
    }

    @Test
    fun testIgnoreCases() {
        assert(
            ProhibitedClassExtendInspection(),
            """<?php
                    /**
                     * final
                     */
                    class Fruit{}
                    class Apple extends Fruit{}
                    class Id implements Fa{}
                """
        )
    }

}
