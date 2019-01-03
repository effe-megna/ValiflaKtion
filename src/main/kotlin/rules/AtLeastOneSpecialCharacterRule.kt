package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import org.example.utils.toMatchRegex

class AtLeastOneSpecialCharacterRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AtLeastOneSpecialCharacter(val message: String = "At least one letter should be a special character.")

    override fun isValid(value: String?): Boolean {
        return value?.toMatchRegex("[A-Za-z0-9]+") ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AtLeastOneSpecialCharacter) {
                AllLowerCaseRule(annotation.message)
            } else null
        }

    }
}