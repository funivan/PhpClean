package com.funivan.idea.phpClean.inspections.redundantDocCommentTag

import com.funivan.idea.phpClean.BaseInspectionTest
import kotlin.test.Test

class RedundantDocCommentTagInspectionTest : BaseInspectionTest() {
    @Test
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
                    function show(string ${'$'}message):void {}
                    """
        )
    }

    @Test
    fun testRedundantReturnTypeWithNonEmptyComment() {
        assert(
            RedundantDocCommentTagInspection(),
            """
                    <?php
                     /**
                      * Hello world
                      *<warning descr="Redundant PhpDoc tag">@return void</warning>
                      */
                     function show(string ${'$'}message):void {}
                    """,
            """
                    <?php
                     /**
                      * Hello world
                      */
                     function show(string ${'$'}message):void {}
                    """
        )
    }

    @Test
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
                    """,
            """
                    <?php
                     /**
                      * @param string ${'$'}test
                      */
                     function show(int ${'$'}a, string ${'$'}message):void {}
                    """
        )
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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
