package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import org.example.utils.toMatchRegex

class RegexRule(val pattern: String, override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class Regex(val pattern: String, val message: String = "Regex pattern doesn't match.")

    override fun isValid(value: String?): Boolean {
        return value?.toMatchRegex(pattern) ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is Regex) {
                RegexRule(annotation.pattern, annotation.message)
            } else null
        }

    }
}