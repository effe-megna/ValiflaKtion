package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class ElementsNotEmptyRule(override val message: String) : IRule<List<String>>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class ElementsNotEmpty(val message: String = "The list cannot contain empty string.")

    override fun isValid(value: List<String>?): Boolean {
        return value?.all { it.isNotEmpty() } ?: false
    }

    companion object Builder : IRuleBuilder<List<String>> {
        override val kType = getKType<List<String>>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<List<String>>? {
            return if (annotation is ElementsNotEmpty) {
                ElementsNotEmptyRule(annotation.message)
            } else null
        }
    }
}