package org.example

import core.Validator
import org.example.core.ISelector
import org.example.core.Selector
import rules.AllUpperCaseRule.AllUpperCase
import rules.EmailRule
import rules.NotBlankRule.NotBlank
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import rules.AssertTrueRule

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
        var isValidModel: Boolean? = null
        val validator = Validator(FooModel()).apply { isValidModel = executeValidation() }

        assertTrue(isValidModel!!)
        assertTrue(validator.constraintViolations.violations.isEmpty())
    }

    @Test fun basicFailedValidation() {
        var isValidModel: Boolean? = null
        val validator = Validator(FooModel(
                firstName = "",
                lastName = "",
                email = "fransisco"
        )).apply { isValidModel = executeValidation() }

        assertFalse(isValidModel!!)
        assertFalse(validator.constraintViolations.violations.isEmpty())
    }

    class Field(val value: String)

    class Model(
            @Selector(FieldSelector::class)
            @AllUpperCase
            val field: Field
    )

    class FieldSelector : ISelector<Field, String> {
        override fun extractValueToValidate(value: Field?): String? {
            return value?.value
        }
    }

    @Test fun validationUsingSelector() {
        var isValidModel: Boolean? = null
        val validator = Validator(Model(Field("ALL"))).apply { isValidModel = executeValidation() }

        assertTrue(isValidModel!!)
    }

    data class FatModel(
            @Selector(FieldSelector::class)
            @NotBlank
            val firstName: Field = Field(""),

            @Selector(FieldSelector::class)
            @NotBlank
            val lastName: Field = Field(""),

            @Selector(FieldSelector::class)
            @NotBlank
            @EmailRule.Email
            val email: Field = Field(""),

            @Selector(FieldSelector::class)
            @NotBlank
            @EmailRule.Email
            val confirmEmail: Field = Field(""),

            @AssertTrueRule.AssertTrue
            val privacyAccepted: Boolean = false
    )

    @Test
    fun testFatModel() {
        var isValidModel = false
        val validator = Validator(FatModel()).apply { isValidModel = executeValidation() }

        assertFalse(isValidModel)
    }

}