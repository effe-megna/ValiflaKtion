package org.example

import core.INotifier
import core.RulesViolations
import core.Validator
import org.example.core.ISelector
import org.example.core.Selector
import org.example.rules.AllLowerCaseRule
import org.example.rules.AllUpperCaseRule
import org.example.rules.AllUpperCaseRule.AllUpperCase
import org.example.rules.EmailRule
import org.example.rules.NotBlankRule.NotBlank
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorTests {

    internal class FooModel(
            firstName: String = "francesco",
            lastName: String = "megna",
            email: String = "francesco.megna97@gmail.com"
    ) {
        @NotBlank
        val firstname = firstName

        @NotBlank
        val lastname = lastName

        @EmailRule.Email
        val email = email
    }

    @Test fun basicSuccessValidation() {
        val validator = Validator.validate(FooModel())

        assertTrue(validator.isValidModel)
        assertTrue(validator.constraintViolations.violations.isEmpty())
    }

    @Test fun basicFailedValidation() {
        val validator = Validator(FooModel(
                firstName = "",
                lastName = "",
                email = "fransisco"
        )).apply { executeValidation() }

        assertFalse(validator.isValidModel)
        assertFalse(validator.constraintViolations.violations.isEmpty())
    }

    class Field(val value: String)

    class Model(
            @Selector(FieldSelector::class)
            @AllUpperCase
            val field: Field
    )

    class FieldSelector : ISelector<Field, String> {
        override fun extractValueToValidate(value: Field): String {
            return value.value
        }
    }

    @Test fun validationUsingSelector() {
        val validator = Validator.validate(Model(Field("ALL")))

        validator.executeValidation()

        assertTrue(validator.isValidModel)
    }
}