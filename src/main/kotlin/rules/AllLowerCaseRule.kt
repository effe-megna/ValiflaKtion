package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class AllLowerCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AllLowerCase(val message: String = "All letters should be in lower case.")

    override fun isValid(value: String?): Boolean {
        return value?.all { it.isLowerCase() } ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AllLowerCase) {
                AllLowerCaseRule(annotation.message)
            } else null
        }

    }
}