package org.example.core

import core.ErrorMessage
import core.RulesViolations.RuleViolated

class PropertyValidator <T : Any> (
        private val property: T,
        private val propertyName: String,
        private val builders: List<IRuleBuilder<in T>>
) {
    fun executeValidations(annotations: List<Annotation>): MutableList<RuleViolated> {
        val violations = mutableListOf<RuleViolated>()

        annotations.forEach { annotation: Annotation ->
            builders.forEach {
                val rule = it.buildFromAnnotation(annotation) ?: return@forEach

                if (rule.isValid(property).not()) {
                    val violation = RuleViolated(rule as IRule<*>, annotation)

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