package com.funivan.idea.phpClean.inspections.parentPropertyDeprecated

import com.funivan.idea.phpClean.BaseInspectionTest
import com.funivan.idea.phpClean.inspections.deprecatedDocTag.ParentPropertyDeprecatedInspection

class ParentPropertyDeprecatedInspectionTest : BaseInspectionTest() {
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
