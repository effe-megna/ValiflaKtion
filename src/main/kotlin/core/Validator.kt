package core

import org.example.core.IRuleBuilder
import org.example.core.PropertyValidator
import org.example.rules.AtLeastOneLowerCaseRule
import org.example.rules.AtLeastOneNumberCaseRule
import org.example.rules.EmailRule
import org.example.rules.NotBlankRule
import kotlin.reflect.full.*

/**
 * This class is responsible to validate your model and exhibits callback through the validation process
 *
 * @param T the type of your model
 * @property model the model you want to validate
 * @property notifier a class or object implements INotifier, useful for callback through the validation process
 * @property constraintViolations object that exhibits info about the failed rules
 * @property ruleBuilders mutableList of RuleBuilder, required for build rule from annotation
 * @property isValidModel boolean represents the result of validation
 *
 */
class Validator <T : Any>(
        private val model: T,
        private val notifier: INotifier? = null
) {
    val constraintViolations = RulesViolations()

    val ruleBuilders: MutableList<IRuleBuilder<out Any>> = mutableListOf(
            NotBlankRule,
            EmailRule,
            AtLeastOneNumberCaseRule,
            AtLeastOneLowerCaseRule
    )

    var isValidModel: Boolean = false
        get() = constraintViolations.violations.isEmpty()
        private set

    fun executeValidation() {
        model::class.memberProperties.forEachIndexed { index, kProperty ->

            val name = kProperty.name
            val annotations = kProperty.annotations

            if (annotations.isEmpty()) return

            val value = readProperty<Any>(model, name)

            // Get builders based on property type, broken when use Selector
            // @Suppress("UNCHECKED_CAST")
            // val buildersBasedOnKClass = ruleBuilders.filter {
            //    kProperty.returnType.isSubtypeOf(it.kType)
            // } as List<IRuleBuilder<in Any>>

            val validationsViolated = PropertyValidator(
                    value,
                    ruleBuilders as List<IRuleBuilder<in Any>>
            ).executeValidations(model::class.memberProperties.elementAt(index).annotations)

            if (validationsViolated.isNotEmpty())
                constraintViolations[kProperty.name, value] = validationsViolated

            notifier?.run {
                if (validationsViolated.isEmpty()) {
                    validationSuccess(name, value, annotations)
                } else {
                    validationFail(name, value, validationsViolated)
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