package com.funivan.idea.phpClean.inspections.propertyAnnotation

import com.funivan.idea.phpClean.BaseInspectionTest


class PropertyAnnotationInspectionTest : BaseInspectionTest() {
    fun testProperyIsNotAnnotatedCorrectly() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class A {
                        /** @var string[] */
                        private <warning descr="Property is not annotated correctly. Add null type">${'$'}first</warning>;
                    }
                    """
        )
    }

    fun testPropertiesWithoutDocumentation() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class A {
                     private ${'$'}name;
                     /**
                      * @deprecated
                      * some description
                      */
                     private ${'$'}id;
                    }
                    """
        )
    }

    fun testPropertyWithCorrectDocumentation() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class A {
                        /** @var string[]|null */
                        private ${'$'}first;

                        /**
                         * @var string|null
                         */
                        private ${'$'}second;
                    }
                """
        )
    }

    fun testAnnotateOwnFieldsOnly() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class B extends A {
                        /**
                         * @var string
                         */
                        private <warning descr="Property is not annotated correctly. Add null type">${'$'}second</warning>;

                        private function getFirst() : array {

                        }
                    }
                """
        )
    }

    fun testPropertiesInFinalClass() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    final class A {
                     /** @var string[]*/
                     public <warning descr="Property is not annotated correctly. Add null type">${'$'}names</warning>;
                     /** @var int */
                     protected <warning descr="Property is not annotated correctly. Add null type">${'$'}id</warning>;
                    }
                    """
        )
    }

    fun testPropertiesInFinalClassWithExtends() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    final class B extends C{
                     /** @var string */
                     protected ${'$'}id; // can be inited in the parent class
                     /** @var int */
                     private <warning descr="Property is not annotated correctly. Add null type">${'$'}age</warning>;
                    }
                    """
        )
    }

    fun testPrivatePropertiesInClassesWithoutConstructor() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class A {
                        /** @var string */
                        private <warning descr="Property is not annotated correctly. Add null type">${'$'}first</warning>;
                    }
                """
        )
    }

    fun testPrivatePropertyNotInitializedInTheConstructor() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class A {
                        /**
                         * @var string
                         */
                        private <warning descr="Property is not annotated correctly. Add null type">${'$'}first</warning>;
                    }
                """
        )
    }

    fun testWithParentConstructor() {
        assert(
                PropertyAnnotationInspection(),
                """
                    <?php
                    class A {
                     public function __construct(){
                     }
                    }
                    class B extends A {
                        /**
                         * @var string
                         */
                        private <warning descr="Property is not annotated correctly. Add null type">${'$'}p</warning>;
                        /** @var string */
                        protected ${'$'}name;
                    }
                """
        )
    }

}