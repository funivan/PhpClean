package com.funivan.idea.phpClean.inspections.missingParameterType

import com.funivan.idea.phpClean.BaseInspectionTest

class MissingParameterTypeDeclarationInspectionTest : BaseInspectionTest() {

    fun testFindMethodsThatCanBePrivate() {
        assert(
                MissingParameterTypeDeclarationInspection(),
                """<?php
                function withName(<warning descr="Missing parameter type">${'$'}name</warning>){}
                """
        )
    }
}