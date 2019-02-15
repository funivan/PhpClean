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
                      *
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
}
