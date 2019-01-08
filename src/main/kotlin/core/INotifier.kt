package core

import core.RulesViolations.RuleViolated

interface INotifier {
    fun validationSuccess(propertyName: String, propertyValue: Any?, annotations: List<Annotation>)

    fun validationFail(propertyName: String, propertyValue: Any?, violations: List<RuleViolated>)

    fun stopValidation():Boolean
}