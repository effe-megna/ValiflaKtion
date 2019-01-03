package core

import org.example.core.IRuleBuilder
import org.example.core.PropertyValidator
import org.example.rules.AtLeastOneLowerCaseRule
import org.example.rules.AtLeastOneNumberCaseRule
import org.example.rules.EmailRule
import org.example.rules.NotBlankRule
import kotlin.reflect.full.*

class Validator <T : Any>(
        private val model: T,
        private val notifier: INotifier? = null
) {
    private val constraintViolations = RulesViolations()

    private val ruleBuilders: MutableList<IRuleBuilder<out Any>> = mutableListOf(
            NotBlankRule,
            EmailRule,
            AtLeastOneNumberCaseRule,
            AtLeastOneLowerCaseRule
    )

    val violations = constraintViolations.violations

    var isValidModel: Boolean = false
        get() = violations.isEmpty()
        private set

    fun executeValidation() {
        model::class.memberProperties.forEachIndexed { index, kProperty ->

            val name = kProperty.name
            val annotations = kProperty.annotations

            if (annotations.isEmpty()) return

            val value = readProperty<Any>(model, name)

            @Suppress("UNCHECKED_CAST")
            val buildersBasedOnKClass = ruleBuilders.filter {
                kProperty.returnType.isSubtypeOf(it.kType)
            } as List<IRuleBuilder<in Any>>

            val validationsViolated = PropertyValidator(
                    value,
                    name,
                    buildersBasedOnKClass
            ).executeValidations(model::class.memberProperties.elementAt(index).annotations)

            if (validationsViolated.isNotEmpty())
                constraintViolations[kProperty.name] = validationsViolated

            notifier?.run {
                if (validationsViolated.isEmpty()) {
                    validationSuccess(name, annotations)
                } else {
                    validationFail(name, validationsViolated)
                }

                if (notifier.stopValidation()) return
            }
        }
    }

    companion object {
        fun <T : Any> validate(
                model: T,
                notifier: INotifier? = null
        ): Validator<T> {
            return Validator(model, notifier).apply {
                executeValidation()
            }
        }

        fun <R: Any?> readProperty(
                instance: Any,
                propertyName: String
        ): R {
            val clazz = instance.javaClass.kotlin
            @Suppress("UNCHECKED_CAST")
            return clazz.declaredMemberProperties.first {
                it.name == propertyName
            }.get(instance) as R
        }
    }
}