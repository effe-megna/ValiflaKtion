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
                .constructoRuleFromBuilderAndProperty(AllLowerCaseRule.Builder)
                .ruleIsValidOnProperty("value")
                .modify {
                    it.foo = "LOWERCASE"
                }
                .ruleIsUnvalidonProperty()
    }

    @Test fun allUpperCase() {
        val fooModel = AllUpperCaseModel("UPPERCASE")

        TestUtils<AllUpperCaseModel>(fooModel)
            .constructoRuleFromBuilderAndProperty(AllUpperCaseRule.Builder)
            .ruleIsValidOnProperty("value")
            .modify {
                it.foo = "uppercase"
            }
            .ruleIsUnvalidonProperty("value")
    }

    @Test fun atLeastOneLowerCase() {
        TestUtils<AtLeastOneLowerCaseModel>(AtLeastOneLowerCaseModel("kCLASS"))
                .constructoRuleFromBuilderAndProperty(AtLeastOneLowerCaseRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "KCLASS"
                }
                .ruleIsUnvalidonProperty()
    }

    @Test fun atLeastOneNumberCase() {
        TestUtils<AtLeastOneNumberCaseModel>(AtLeastOneNumberCaseModel("asd1"))
                .constructoRuleFromBuilderAndProperty(AtLeastOneNumberCaseRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "asd"
                }
                .ruleIsUnvalidonProperty()
    }

    @Test fun atLeastOneSpecialCharacter() {
        TestUtils<AtLeastOneSpecialCharacterModel>(AtLeastOneSpecialCharacterModel("password#"))
                .constructoRuleFromBuilderAndProperty(AtLeastOneSpecialCharacterRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "password"
                }
                .ruleIsUnvalidonProperty()
    }

    @Test fun atLeastOneUpperCase() {
        TestUtils<AtLeastOneUpperCaseModel>(AtLeastOneUpperCaseModel("Pass"))
                .constructoRuleFromBuilderAndProperty(AtLeastOneUpperCaseRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "fail"
                }
                .ruleIsUnvalidonProperty()
    }

    @Test fun noNumbers() {
        TestUtils<NoNumberModel>(NoNumberModel("pass"))
                .constructoRuleFromBuilderAndProperty(NoNumbersRule)
                .ruleIsValidOnProperty()
                .modify {
                    it.foo = "1_fail"
                }
                .ruleIsUnvalidonProperty()
    }


    class TestUtils <T> (private val model: Any) {

        var rule: IRule<*>? = null

        fun constructoRuleFromBuilderAndProperty(
                ruleBuilder: IRuleBuilder<*>,
                propertyName: String = "value"
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

        fun ruleIsValidOnProperty(propertyName: String = "value"): TestUtils<T> {
            assertTrue(rule!!.isValid(Validator.readProperty(model, propertyName)))

            return this
        }

        fun ruleIsUnvalidonProperty(propertyName: String = "value"): TestUtils<T> {
            assertFalse(rule!!.isValid(Validator.readProperty(model, propertyName)))

            return this
        }
    }
}