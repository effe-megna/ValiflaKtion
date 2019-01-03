package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class AllUpperCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AllUpperCase(val message: String = "All letters should be in upper case.")

    override fun isValid(value: String?): Boolean {
        return value?.all { it.isUpperCase() } ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AllUpperCase) {
                AllUpperCaseRule(annotation.message)
            } else null
        }

    }
}