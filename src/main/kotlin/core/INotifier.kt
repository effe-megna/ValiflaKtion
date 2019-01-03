package core

import core.RulesViolations.RuleViolated

interface INotifier {
    fun validationSuccess(propertyName: String, annotations: List<Annotation>)

    fun validationFail(propertyName: String, violations: List<RuleViolated>)

    fun stopValidation():Boolean
}