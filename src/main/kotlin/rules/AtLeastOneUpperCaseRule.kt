package rules

import core.ErrorMessage
import extensions.containsAtLeastOneUpperCase
import org.example.core.IRule
import org.example.core.IRuleBuilder

class AtLeastOneUpperCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AtLeastOneUpperCase(val message: String = "At least one letter should be in upper case.")

    override fun isValid(value: String?): Boolean {
        return value?.containsAtLeastOneUpperCase() ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AtLeastOneUpperCase) {
                AtLeastOneUpperCaseRule(annotation.message)
            } else null
        }

    }
}