package com.funivan.idea.phpClean.inspections.toStringCall

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class ToStringCallIssue212Test : BaseInspectionTest() {
    @Test
    fun testCastFunctionCallFromObject() {
        assert(
            ToStringCallInspection(),
            """
                    <?php
                    class BlaFoo {
                        public function __toString(): string {
                            return 'BlaFoo';
                        }
                    }
                    class Another {
                        public function returnsBlaFoo(): BlaFoo {
                            return new BlaFoo();
                        }
                        public function __toString(): string {
                            return '';
                        }
                    }
                    ${'$'}another = new Another();
                    (string)(${ '$'}another->returnsBlaFoo());
                    (string)${'$'}another->returnsBlaFoo();
        )
    }
}
