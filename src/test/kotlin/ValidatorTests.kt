package org.example

import core.Validator
import org.example.rules.EmailRule
import org.example.rules.NotBlankRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorTests {

    internal class FooModel(
            firstName: String = "francesco",
            lastName: String = "megna",
            email: String = "francesco.megna97@gmail.com"
    ) {
        @NotBlankRule.NotBlank
        val firstname = firstName

        @NotBlankRule.NotBlank
        val lastname = lastName

        @EmailRule.Email
        val email = email
    }

    @Test fun basicSuccessValidation() {
        val validator = Validator.validate(FooModel())

        assertTrue(validator.isValidModel)
        assertTrue(validator.violations.isEmpty())
    }

    @Test fun basicFailedValidation() {
        val validator = Validator(FooModel(
                firstName = "",
                lastName = "",
                email = "fransisco"
        )).apply { executeValidation() }

        assertFalse(validator.isValidModel)
        assertFalse(validator.violations.isEmpty())
    }
}