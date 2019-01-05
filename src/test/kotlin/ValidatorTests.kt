package org.example

import core.Validator
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
}