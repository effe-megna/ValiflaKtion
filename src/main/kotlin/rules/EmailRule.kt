package org.example.rules

import core.ErrorMessage
import extensions.isEmail
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import org.example.utils.toMatchRegex

class EmailRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class Email(val message : String = "Invalid Email address.")

    override fun isValid(value: String?): Boolean {
        return value?.isEmail() ?: false
    }

    companion object Builder : IRuleBuilder<String>{
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is Email) {
                EmailRule(annotation.message)
            } else null
        }
    }
}