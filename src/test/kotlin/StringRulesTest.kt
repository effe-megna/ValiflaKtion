package org.example

import core.Validator
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.rules.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.full.memberProperties

class StringRulesTest {
    internal class AllLowerCaseModel(foo: String) {
        @AllLowerCaseRule.AllLowerCase
        var foo = foo
    }

    @Test fun allLowerCase() {
        TestUtils<AllLowerCaseModel>(AllLowerCaseModel("lowercase"))
                .grabRule(AllLowerCaseRule.Builder)
                .assertTrueOnRule("foo")
                .modify {
                    it.foo = "LOWERCASE"
                }
                .assertFalseOnRule()
    }

    internal class AllUpperCaseModel(foo: String) {
        @AllUpperCaseRule.AllUpperCase
        var foo = foo
    }

    @Test fun allUpperCase() {
        val fooModel = AllUpperCaseModel("UPPERCASE")

        TestUtils<AllUpperCaseModel>(fooModel)
            .grabRule(AllUpperCaseRule.Builder)
            .assertTrueOnRule("foo")
            .modify {
                it.foo = "uppercase"
            }
            .assertFalseOnRule("foo")
    }

    internal class AtLeastOneLowerCaseModel(foo: String) {
        @AtLeastOneLowerCaseRule.AtLeastOneLowerCase
        var foo = foo
    }

    @Test fun atLeastOneLowerCase() {
        TestUtils<AtLeastOneLowerCaseModel>(AtLeastOneLowerCaseModel("kCLASS"))
                .grabRule(AtLeastOneLowerCaseRule)
                .assertTrueOnRule()
                .modify {
                    it.foo = "KCLASS"
                }
                .assertFalseOnRule()
    }

    internal class AtLeastOneNumberCaseModel(foo: String) {
        @AtLeastOneNumberCaseRule.AtLeastOneNumberCase
        var foo = foo
    }

    @Test fun atLeastOneNumberCase() {
        TestUtils<AtLeastOneNumberCaseModel>(AtLeastOneNumberCaseModel("asd1"))
                .grabRule(AtLeastOneNumberCaseRule)
                .assertTrueOnRule()
                .modify {
                    it.foo = "asd"
                }
                .assertFalseOnRule()
    }

    internal class AtLeastOneSpecialCharacterModel(foo: String) {
        @AtLeastOneSpecialCharacterRule.AtLeastOneSpecialCharacter
        var foo = foo
    }

    @Test fun atLeastOneSpecialCharacter() {
        TestUtils<AtLeastOneSpecialCharacterModel>(AtLeastOneSpecialCharacterModel("password#"))
                .grabRule(AtLeastOneSpecialCharacterRule)
                .assertTrueOnRule()
                .modify {
                    it.foo = "password"
                }
                .assertFalseOnRule()
    }

    internal class AtLeastOneUpperCaseModel(foo: String) {
        @AtLeastOneUpperCaseRule.AtLeastOneUpperCase
        var foo = foo
    }

    @Test fun atLeastOneUpperCase() {
        TestUtils<AtLeastOneUpperCaseModel>(AtLeastOneUpperCaseModel("Pass"))
                .grabRule(AtLeastOneUpperCaseRule)
                .assertTrueOnRule()
                .modify {
                    it.foo = "fail"
                }
                .assertFalseOnRule()
    }


    class TestUtils <T> (private val model: Any) {

        var rule: IRule<*>? = null

        fun grabRule(
                ruleBuilder: IRuleBuilder<*>,
                propertyName: String = "foo"
        ): TestUtils<T> {
            val annotation = model::class.memberProperties.find { it.name == propertyName }!!.annotations.first()

            rule = ruleBuilder.buildFromAnnotation(annotation)

            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun modify(execute: (T) -> Unit): TestUtils<T> {
            execute(model as T)

            return this
        }

        fun assertTrueOnRule(propertyName: String = "foo"): TestUtils<T> {
            assertTrue(rule!!.isValid(Validator.readProperty(model, propertyName)))

            return this
        }

        fun assertFalseOnRule(propertyName: String = "foo"): TestUtils<T> {
            assertFalse(rule!!.isValid(Validator.readProperty(model, propertyName)))

            return this
        }
    }
}