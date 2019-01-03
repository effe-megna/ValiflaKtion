package org.example.core

import core.ValidationViolated
import core.ErrorMessage


class PropertyValidator <T : Any> (
        private val property: T,
        private val propertyName: String,
        private val builders: List<IRuleBuilder<in T>>
) {
    fun executeValidations(annotations: List<Annotation>): MutableList<ValidationViolated> {
        val violations = mutableListOf<ValidationViolated>()

        annotations.forEach { annotation: Annotation ->
            builders.forEach {
                val rule = it.buildFromAnnotation(annotation) ?: return@forEach

                if (rule.isValid(property).not()) {
                    val violation = ValidationViolated(rule as IRule<*>, annotation)

                    if (rule is ErrorMessage) {
                        violation.message = rule.message
                    }

                    violations += violation
                }
            }
        }

        return violations
    }
}