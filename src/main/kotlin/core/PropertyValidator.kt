package org.example.core

import core.ErrorMessage
import core.RulesViolations.RuleViolated
import kotlin.reflect.full.createInstance

class PropertyValidator <T : Any> (
        private val value: T,
        private val builders: List<IRuleBuilder<in T>>
) {
    fun executeValidations(annotations: List<Annotation>): MutableList<RuleViolated> {
        val violations = mutableListOf<RuleViolated>()

        val selector = annotations.singleOrNull { it is Selector } as Selector?

        var valueToValidate: T? = value

        if (selector != null) {
            val selectorImplementation = selector.selector.createInstance() as ISelector<Any, Any>

            valueToValidate = selectorImplementation.extractValueToValidate(value) as T
        }

        annotations.forEach { annotation: Annotation ->

            if (annotation is Selector) return@forEach

            builders.forEach {
                val rule = it.buildFromAnnotation(annotation) ?: return@forEach

                if (rule.isValid(valueToValidate).not()) {
                    val violation = RuleViolated(rule as IRule<*>, annotation)

                    if (rule is ErrorMessage) violation.message = rule.message

                    violations += violation
                }
            }
        }

        return violations
    }
}