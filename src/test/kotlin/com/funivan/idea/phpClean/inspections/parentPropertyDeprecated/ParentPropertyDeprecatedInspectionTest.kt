package com.funivan.idea.phpClean.inspections.parentPropertyDeprecated

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class ParentPropertyDeprecatedInspectionTest : BaseInspectionTest() {
    @Test
    fun testDeprecatedParentProperty() {
        assert(
            ParentPropertyDeprecatedInspection(),
            """
                    <?php
                    class A {
                        /** @deprecated */
                        public ${'$'}name;
                    }
                    class B extends A{
                        public <warning  descr="Parent property is deprecated">${'$'}name</warning>;
                    }
                    """
        )
    }

    @Test
    fun testAllPropertiesAreDeprecated() {
        assert(
            ParentPropertyDeprecatedInspection(),
            """
                    <?php
                    class A {
                        /** @deprecated */
                        public ${'$'}name;
                    }
                    class B extends A{
                        /** @deprecated */
                        public ${'$'}name;
                    }
                    """
        )
    }

    @Test
    fun testNonDeprecatedProperties() {
        assert(
            ParentPropertyDeprecatedInspection(),
            """
                    <?php
                    class A { public ${'$'}name; }
                    class B extends A{ public ${'$'}name; }
                    class C { protected ${'$'}name; }
                    """
        )
    }
}
