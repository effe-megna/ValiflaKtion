package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
class AssertFalseRule(override val message: String) : IRule<Boolean>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AssertFalse(val message: String = "The boolean should be false.")

    override fun isValid(value: Boolean?): Boolean {
        return if (value != null) {
            value == true
        } else false
    }

    companion object Builder : IRuleBuilder<Boolean> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<Boolean>? {
            return if (annotation is AssertFalse) {
                AssertFalseRule(annotation.message)
            } else null
        }
    }
}