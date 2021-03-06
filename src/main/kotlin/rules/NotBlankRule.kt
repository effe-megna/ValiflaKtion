package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder

class NotBlankRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class NotBlank(val message: String = "Can't be empty.")

    override fun isValid(value: String?): Boolean {
        return value?.isNotBlank() ?: false
    }

    companion object Builder: IRuleBuilder<String> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is NotBlank) {
                NotBlankRule(annotation.message)
            } else null
        }
    }
}