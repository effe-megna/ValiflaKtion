package rules

import core.ErrorMessage
import extensions.containOnlyLowerCase
import org.example.core.IRule
import org.example.core.IRuleBuilder

class AllLowerCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AllLowerCase(val message: String = "All letters should be in lower case.")

    override fun isValid(value: String?): Boolean {
        return value?.containOnlyLowerCase() ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AllLowerCase) {
                AllLowerCaseRule(annotation.message)
            } else null
        }

    }
}