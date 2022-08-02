package com.funivan.idea.phpClean.inspections.classNameCollision

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class ClassNameCollisionInspectionTest : BaseInspectionTest() {

    @Test
    fun testClassNameCollision() {
        assert(
            ClassNameCollisionInspection(),
            """<?php
                    namespace App {
                      class <warning descr="Class name collision with \Cli\User">User</warning>{};
                    }
                    namespace Cli {
                        class <warning descr="Class name collision with \App\User">User</warning>{};
                    }
                """
        )
    }

    @Test
    fun testCollisionInGlobalNamespace() {
        assert(
            ClassNameCollisionInspection(),
            """<?php
                    namespace {
                      class <warning descr="Class name collision with \App\User">User</warning>{};
                    }
                    namespace App {
                      class <warning descr="Class name collision with \User">User</warning>{};
                    }
                """
        )
    }
}
