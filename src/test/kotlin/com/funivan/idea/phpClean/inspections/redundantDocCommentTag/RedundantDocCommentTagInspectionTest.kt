package com.funivan.idea.phpClean.inspections.redundantDocCommentTag

import com.funivan.idea.phpClean.BaseInspectionTest

class RedundantDocCommentTagInspectionTest : BaseInspectionTest() {
    fun testRedundantReturnType() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                     /**
                      *<warning descr="Redundant PhpDoc tag">@return void</warning>
                      */
                     function show(string ${'$'}message):void {}
                    """,
                """
                    <?php
                     /**
                      */
                     function show(string ${'$'}message):void {}
                    """
        )
    }

    fun testRedundantParameterTag() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                     /**
                      *<warning descr="Redundant PhpDoc tag">@param string ${'$'}message</warning>
                      * @param string ${'$'}test
                      */
                     function show(int ${'$'}a, string ${'$'}message):void {}
                    """
        )
    }

    fun testRedundantParameterTagWithClassFQN() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                    class A{
                     /**
                      *<warning descr="Redundant PhpDoc tag">@param \stdClass ${'$'}c</warning>
                      * @param string ${'$'}a
                      */
                     function isObject(${'$'}a, ${'$'}b, stdClass ${'$'}c):bool {}
                     }
                    """
        )
    }

    fun testReturnMultipleTypes() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                     /**
                      * @return \Generator|string[]
                      */
                     function names():\Generator {yield "";}
                    """
        )
    }

    fun testCheckEmptyTag() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                     /**
                      * @return  
                      */
                     function name():string {return "";}
                    """
        )
    }
    fun testCheckNullableReturnType() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                     /**
                      *<warning descr="Redundant PhpDoc tag">@return bool|null</warning>
                      */
                     function show():?bool {}
                    """
        )
    }

    fun testCheckNullableParameterType() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                     /**
                      *<warning descr="Redundant PhpDoc tag">@param stdClass|null ${'$'}o</warning>
                      */
                     function show(?stdClass ${'$'}o) {}
                    """
        )
    }

    fun testRedundantFieldTag() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                    class PhpClean {
                        /**
                         * <warning descr="Redundant PhpDoc tag">@var bool</warning>
                         */ 
                        private bool ${'$'}isClean = false;                        
                    }
                    """
        )
    }

    fun testFieldTagWithClassFQN() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                    class PhpClean {
                        /**
                         * <warning descr="Redundant PhpDoc tag">@var \stdClass</warning>
                         */ 
                        private \stdClass ${'$'}cleaner;                        
                    }
                    """
        )
    }

    fun testFieldTagWithMultipleTypes() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                    class PhpClean {
                        /**
                         * @var \Generator|string[]
                         */ 
                        private \Generator ${'$'}rules;                        
                    }
                    """
        )
    }

    fun testFieldWithEmptyTag() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                    class PhpClean {
                        /**
                         * @var
                         */ 
                        private \Generator ${'$'}rules;                        
                    }
                    """
        )
    }

    fun testCheckNullableFieldType() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                    class PhpClean {
                        /**
                         * <warning descr="Redundant PhpDoc tag">@var \stdClass|null</warning>
                         */ 
                        private ?\stdClass ${'$'}cleaner;                          
                    }
                    """
        )
    }

    fun testGenericType() {
        assert(
                RedundantDocCommentTagInspection(),
                """
                    <?php
                    class PhpClean {
                     /**
                      * @var array[]
                      */
                     private array ${'$'}a;
                       /**
                        * @param string[]
                        * @return array<string>
                        */
                       public function get(array ${'$'}items):array {
                       }
                    }
                    """
        )
    }
}
