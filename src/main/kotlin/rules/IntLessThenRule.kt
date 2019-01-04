package org.example.rules

import core.ErrorMessage
import org.example.core.IRule
import org.example.core.IRuleBuilder
import org.example.utils.getKType

class IntLessThenRule(val target: Int, override val message: String) : IRule<Int>, ErrorMessage {
    @Target(AnnotationTarget.PROPERTY)
    annotation class IntLessThen(val target: Int, val message: String = "The value should be less then the target.")

    //TODO find a way to use target value into default message

    override fun isValid(value: Int?): Boolean {
        return if (value != null) {
            return value < target
        } else false
    }

    companion object Builder : IRuleBuilder<Int>{
        override val kType = getKType<Int>()

        override fun buildFromAnnotation(annotation: Annotation): IRule<Int>? {
            return if (annotation is IntLessThen) {
                IntLessThenRule(annotation.target, annotation.message)
            } else null
        }
    }
}
