package core

import org.example.core.IRule

class RulesViolations {
    var violations = mutableListOf<ConstraintViolation>()

    operator fun get(propertyName: String): ConstraintViolation? {
        return violations.singleOrNull { it.propertyName == propertyName }
    }

    operator fun set(propertyName: String, rulesViolated: List<RuleViolated>) {
        if (violations.any { it.propertyName == propertyName }.not()) {
            violations.add(ConstraintViolation(propertyName, mutableListOf()))
        }

        violations.single {
            it.propertyName == propertyName
        }.apply {
            this.rulesViolated += rulesViolated
        }
    }

    class ConstraintViolation(
            val propertyName: String,
            val rulesViolated: MutableList<RuleViolated>
    )

    class RuleViolated(
            rule: IRule<*>,
            val annotation: Annotation,
            var message: String = "Message not provided"
    ) {
        val strategyName = rule.javaClass.name
    }
}
