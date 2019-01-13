package org.example

import core.Validator
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import rules.*
import kotlin.reflect.full.memberProperties

class StringRulesTest {
    internal class AllLowerCaseModel(@AllLowerCaseRule.AllLowerCase var foo: String)
    internal class AllUpperCaseModel(@AllUpperCaseRule.AllUpperCase var foo: String)
    internal class AtLeastOneLowerCaseModel(@AtLeastOneLowerCaseRule.AtLeastOneLowerCase var foo: String)
    internal class AtLeastOneNumberCaseModel(@AtLeastOneNumberCaseRule.AtLeastOneNumberCase var foo: String)
    internal class AtLeastOneSpecialCharacterModel(@AtLeastOneSpecialCharacterRule.AtLeastOneSpecialCharacter var foo: String)
    internal class AtLeastOneUpperCaseModel(@AtLeastOneUpperCaseRule.AtLeastOneUpperCase var foo: String)
    internal class NoNumberModel(@NoNumbersRule.NoNumbers var foo: String)

    @Test fun allLowerCase() {
        TestUtils<AllLowerCaseModel>(AllLowerCaseModel("lowercase"))
                .constructRuleFromBuildernAndProperty(AllLowerCaseRule.Builder)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "LOWERCASE"
                }
                .ruleIsNotValidOnProperty()
    }

    @Test fun allUpperCase() {
        val fooModel = AllUpperCaseModel("UPPERCASE")

        TestUtils<AllUpperCaseModel>(fooModel)
            .constructRuleFromBuildernAndProperty(AllUpperCaseRule.Builder)
            .ruleIsValidOnProperty()
            .modify {
                it.foo = "uppercase"
            }
            .ruleIsNotValidOnProperty()
    }

    @Test fun atLeastOneLowerCase() {
        TestUtils<AtLeastOneLowerCaseModel>(AtLeastOneLowerCaseModel("kCLASS"))
                .constructRuleFromBuildernAndProperty(AtLeastOneLowerCaseRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "KCLASS"
                }
                .ruleIsNotValidOnProperty()
    }

    @Test fun atLeastOneNumberCase() {
        TestUtils<AtLeastOneNumberCaseModel>(AtLeastOneNumberCaseModel("asd1"))
                .constructRuleFromBuildernAndProperty(AtLeastOneNumberCaseRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "asd"
                }
                .ruleIsNotValidOnProperty()
    }

    @Test fun atLeastOneSpecialCharacter() {
        TestUtils<AtLeastOneSpecialCharacterModel>(AtLeastOneSpecialCharacterModel("password#"))
                .constructRuleFromBuildernAndProperty(AtLeastOneSpecialCharacterRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "password"
                }
                .ruleIsNotValidOnProperty()
    }

    @Test fun atLeastOneUpperCase() {
        TestUtils<AtLeastOneUpperCaseModel>(AtLeastOneUpperCaseModel("Pass"))
                .constructRuleFromBuildernAndProperty(AtLeastOneUpperCaseRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "fail"
                }
                .ruleIsNotValidOnProperty()
    }

    @Test fun noNumbers() {
        TestUtils<NoNumberModel>(NoNumberModel("pass"))
                .constructRuleFromBuildernAndProperty(NoNumbersRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "1_fail"
                }
                .ruleIsNotValidOnProperty()
    }


    class TestUtils <T> (private val model: Any) {

        var rule: IRule<*>? = null

        fun constructRuleFromBuildernAndProperty(
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

        fun ruleIsValidOnProperty(propertyName: String = "foo"): TestUtils<T> {
            assertTrue(rule!!.isValid(Validator.readProperty(model, propertyName)))

            return this
        }

        fun ruleIsNotValidOnProperty(propertyName: String = "foo"): TestUtils<T> {
            assertFalse(rule!!.isValid(Validator.readProperty(model, propertyName)))

            return this
        }
    }
}