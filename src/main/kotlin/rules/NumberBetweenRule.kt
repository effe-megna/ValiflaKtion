package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class NumberBetweenRule(
        private val lower: Int,
        private val greater: Int,
        override val message: String
) : IRule<Int>, ErrorMessage {

    @Target(AnnotationTarget.PROPERTY)
    annotation class NumberBetween(val lower: Int = 0, val greater: Int, val message: String = "The number should be between the lower and the greater.")

    override fun isValid(value: Int?): Boolean {
        if (value == null)
            return false

        return value in lower..greater
    }

    companion object Builder : IRuleBuilder<Int> {
        override val kType = getKType<Int>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<Int>? {
            return if (annotation is NumberBetween) {
                NumberBetweenRule(annotation.lower, annotation.greater, annotation.message)
            } else null
        }
    }
}