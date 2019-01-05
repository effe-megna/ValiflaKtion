package org.example.rules

import core.ErrorMessage
import extensions.containsAtLeastOneUpperCase
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import org.example.utils.toMatchRegex

class AtLeastOneUpperCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AtLeastOneUpperCase(val message: String = "At least one letter should be in upper case.")

    override fun isValid(value: String?): Boolean {
        return value?.containsAtLeastOneUpperCase() ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AtLeastOneUpperCase) {
                AtLeastOneUpperCaseRule(annotation.message)
            } else null
        }

    }
}