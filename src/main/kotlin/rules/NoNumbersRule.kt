package rules

import core.ErrorMessage
import extensions.containAnyNumber
import org.example.core.IRule
import org.example.core.IRuleBuilder

class NoNumbersRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class NoNumbers(val message: String = "Should not contain any numbers.")

    override fun isValid(value: String?): Boolean {
        // TODO regex problem
        return value?.containAnyNumber() ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is NoNumbers) {
                NoNumbersRule(annotation.message)
            } else null
        }

    }
}