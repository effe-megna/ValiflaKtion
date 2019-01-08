package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class MinLenghtRule(val target: Int, override val message: String) : IRule<String>, ErrorMessage {
    annotation class MinLength(val target: Int, val message: String = "Length should be greater than target")

    override fun isValid(value: String?): Boolean {
        return if (value != null) {
            value.length > target
        } else false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is MinLength) {
                MinLenghtRule(annotation.target, annotation.message)
            } else null
        }
    }
}