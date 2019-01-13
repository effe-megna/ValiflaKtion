package rules

import core.ErrorMessage
import extensions.containOnlyUpperCase
import org.example.core.IRule
import org.example.core.IRuleBuilder
class AllUpperCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AllUpperCase(val message: String = "All letters should be in upper case.")

    override fun isValid(value: String?): Boolean {
        return value?.containOnlyUpperCase() ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AllUpperCase) {
                AllUpperCaseRule(annotation.message)
            } else null
        }

    }
}