package rules

import core.ErrorMessage
import extensions.containAtLeastOneSpecialCharacter
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class AtLeastOneSpecialCharacterRule(override val message: String) : IRule<String>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AtLeastOneSpecialCharacter(val message: String = "At least one letter should be a special character.")

    override fun isValid(value: String?): Boolean {
        // TODO regex fail
        return value?.containAtLeastOneSpecialCharacter() ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override val kType = getKType<String>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is AtLeastOneSpecialCharacter) {
                AtLeastOneSpecialCharacterRule(annotation.message)
            } else null
        }

    }
}