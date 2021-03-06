package rules

import core.ErrorMessage
import extensions.isEmail
import org.example.core.IRule
import org.example.core.IRuleBuilder

class EmailRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class Email(val message : String = "Invalid Email address.")

    override fun isValid(value: String?): Boolean {
        return value?.isEmail() ?: false
    }

    companion object Builder : IRuleBuilder<String>{
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is Email) {
                EmailRule(annotation.message)
            } else null
        }
    }
}