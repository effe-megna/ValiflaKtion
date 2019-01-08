package rules

import core.ErrorMessage
import extensions.containAtLeastOneNumberCase
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class AtLeastOneNumberCaseRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AtLeastOneNumberCase(val message: String = "At least one letter should be a number.")

    override fun isValid(value: String?): Boolean {
        return value?.containAtLeastOneNumberCase() ?: false
    }

    companion object Builder : IRuleBuilder<String>{
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AtLeastOneNumberCase) {
                AtLeastOneNumberCaseRule(annotation.message)
            } else null
        }
    }
}