package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
class AssertTrueRule(override val message: String) : IRule<Boolean>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AssertTrue(val message: String = "The boolean should be true.")

    override fun isValid(value: Boolean?): Boolean {
        return if (value != null) {
            value == true
        } else false
    }

    companion object Builder : IRuleBuilder<Boolean> {
       override fun buildFromAnnotation(annotation: Annotation): IRule<Boolean>? {
            return if (annotation is AssertTrue) {
                AssertTrueRule(annotation.message)
            } else null
        }
    }
}