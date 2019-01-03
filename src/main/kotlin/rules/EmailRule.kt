package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import org.example.utils.toMatchRegex

class EmailRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class Email(val message : String = "Invalid Email address.")

    override fun isValid(value: String?): Boolean {
        return value?.toMatchRegex("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ) ?: false
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