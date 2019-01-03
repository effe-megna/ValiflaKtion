package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class NotBlankRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class NotBlank(val message: String = "Can't be empty.")

    override fun isValid(value: String?): Boolean {
        return value?.isNotBlank() ?: false
    }

    companion object Builder: IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is NotBlank) {
                NotBlankRule(annotation.message)
            } else null
        }
    }
}