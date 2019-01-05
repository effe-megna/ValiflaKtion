package org.example.rules

import core.ErrorMessage
import extensions.containAtLeastOneLowerCase
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import org.example.utils.toMatchRegex

class AtLeastOneLowerCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AtLeastOneLowerCase(val message: String = "At least one letter should be in lower case.")

    override fun isValid(value: String?): Boolean {
        return value?.containAtLeastOneLowerCase() ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AtLeastOneLowerCase) {
                AtLeastOneLowerCaseRule(annotation.message)
            } else null
        }
    }
}