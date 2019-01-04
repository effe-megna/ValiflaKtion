package core

import org.example.core.IRule

class RulesViolations {
    var violations = mutableListOf<ConstraintViolation<*>>()

    operator fun get(propertyName: String): ConstraintViolation<*>? {
        return violations.singleOrNull { it.propertyName == propertyName }
    }

    operator fun set(propertyName: String, propertyValue: Any, rulesViolated: List<RuleViolated>) {
        if (violations.any { it.propertyName == propertyName }.not()) {
            violations.add(ConstraintViolation(propertyName, propertyValue, mutableListOf()))
        }

        violations.single {
            it.propertyName == propertyName
        }.apply {
            this.rulesViolated += rulesViolated
        }
    }

    class ConstraintViolation <T> (
            val propertyName: String,
            val propertyValue: T,
            val rulesViolated: MutableList<RuleViolated>
    )

    class RuleViolated(
            val rule: IRule<*>,
            val annotation: Annotation,
            var message: String = "Message not provided"
    ) {
        val ruleName = rule.javaClass.name
    }
}
