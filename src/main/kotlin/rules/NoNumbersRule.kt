package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import org.example.utils.toMatchRegex

class NoNumbersRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class NoNumbers(val message: String = "Should not contain any numbers.")

    override fun isValid(value: String?): Boolean {
        // TODO regex problem
        return value?.matches(Regex(".*\\d+.*\"")) ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is NoNumbers) {
                NoNumbersRule(annotation.message)
            } else null
        }

    }
}