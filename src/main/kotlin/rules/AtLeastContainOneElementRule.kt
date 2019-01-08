package rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType
import kotlin.reflect.KType

class AtLeastContainOneElementRule(override val message: String) : IRule<List<Any>>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class AtLeastContainOneElement(val message: String = "The list should contain at least one element.")

    override fun isValid(value: List<Any>?): Boolean {
        return value?.isNotEmpty() ?: false
    }

    companion object Builder : IRuleBuilder<List<Any>> {
        override val kType: KType = getKType<List<Any>>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<List<Any>>? {
            return if (annotation is AtLeastContainOneElement) {
                AtLeastContainOneElementRule(annotation.message)
            } else null
        }
    }
}