package org.example.core

import core.ErrorMessage
import core.RulesViolations.RuleViolated

class PropertyValidator <T : Any> (
        private val value: T,
        private val builders: List<IRuleBuilder<in T>>
) {
    fun executeValidations(annotations: List<Annotation>): MutableList<RuleViolated> {
        val violations = mutableListOf<RuleViolated>()

        annotations.forEach { annotation: Annotation ->
            builders.forEach {
                val rule = it.buildFromAnnotation(annotation) ?: return@forEach

                if (rule.isValid(value).not()) {
                    val violation = RuleViolated(rule as IRule<*>, annotation)

                    if (rule is ErrorMessage) violation.message = rule.message

                    violations += violation
                }
            }
        }

        return violations
    }
}
