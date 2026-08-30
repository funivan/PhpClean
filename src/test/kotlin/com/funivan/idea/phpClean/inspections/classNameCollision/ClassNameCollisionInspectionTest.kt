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

    @Test
    fun testVendorCollisionIgnoredWhenOptionEnabled() {
        myFixture.addFileToProject(
            "vendor/lib/User.php",
            """<?php
                namespace Vendor {
                    class User {};
                }
            """
        )
        val inspection = ClassNameCollisionInspection()
        inspection.ignoreVendorClasses = true
        assert(
            inspection,
            """<?php
                namespace App {
                    class User {};
                }
            """
        )
    }

    @Test
    fun testVendorCollisionReportedWhenOptionDisabled() {
        myFixture.addFileToProject(
            "vendor/lib/User.php",
            """<?php
                namespace Vendor {
                    class User {};
                }
            """
        )
        assert(
            ClassNameCollisionInspection(),
            """<?php
                namespace App {
                    class <warning descr="Class name collision with \Vendor\User">User</warning> {};
                }
            """
        )
    }
}
