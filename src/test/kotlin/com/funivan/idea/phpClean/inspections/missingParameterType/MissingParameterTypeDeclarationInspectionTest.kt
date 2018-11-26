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
}