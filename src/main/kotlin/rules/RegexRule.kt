package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import utils.toMatchRegex

class RegexRule(val pattern: String, override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class Regex(val pattern: String, val message: String = "Regex pattern doesn't match.")

    override fun isValid(value: String?): Boolean {
        return value?.toMatchRegex(pattern) ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is Regex) {
                RegexRule(annotation.pattern, annotation.message)
            } else null
        }
    }
}