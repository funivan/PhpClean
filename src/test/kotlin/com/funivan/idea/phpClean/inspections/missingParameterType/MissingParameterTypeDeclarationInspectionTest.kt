package com.funivan.idea.phpClean.inspections.missingParameterType

import com.funivan.idea.phpClean.BaseInspectionTest

class MissingParameterTypeDeclarationInspectionTest : BaseInspectionTest() {

    fun testMissingParameterType() {
        assert(
                MissingParameterTypeDeclarationInspection(),
                """<?php
                class User{
                    function withName(<warning descr="Missing parameter type">${'$'}name</warning>){}
                }
                """
        )
    }

    fun testMethodWithParameterTypes() {
        assert(
                MissingParameterTypeDeclarationInspection(),
                """<?php
                class User{
                    function withName(string ${'$'}name){}
                }
                """
        )
    }

    fun testIgnoreParameter() {
        assert(
                MissingParameterTypeDeclarationInspection(),
                """<?php
                class User{
                    /**
                     * @param string ${'$'}name @Suppress(MissingParameterTypeDeclarationInspection)
                     */
                    function withId(${'$'}name){}
                }
                """
        )
    }

    fun testInvalidPsi() {
        assert(
                MissingParameterTypeDeclarationInspection(),
                """<?php
                class User{
                    abstract function withId(<error descr="Expected: variable">,</error><error descr="Unexpected: )">)</error>
                }
                """
        )
    }
    fun testSkipOverwrittenMethods() {
        assert(
                MissingParameterTypeDeclarationInspection(),
                """<?php
                 interface Uid{
                    function hash(<warning descr="Missing parameter type">${'$'}salt</warning>);
                 }
                 class Id implements Uid{
                    function with(<warning descr="Missing parameter type">${'$'}id</warning>){}
                 }
                 class TrimmedId extends Id{
                    function with(${'$'}id){}
                    function hash(${'$'}salt){}
                    function rebuild(<warning descr="Missing parameter type">${'$'}uid</warning>){}
                 }
                """
        )
    }
}