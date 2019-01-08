package core

import org.example.core.IRuleBuilder
import org.example.core.PropertyValidator
import rules.*
import kotlin.reflect.full.*

/**
 * This class is responsible to validate your model and exhibits callback through the validation process
 *
 * @param T the type of your model
 * @property model the model you want to validate
 * @property notifier a class or object implements INotifier, useful for callback through the validation process
 * @property constraintViolations object that exhibits info about the failed rules
 * @property ruleBuilders mutableList of RuleBuilder, required for build rule from annotation
 *
 */
class Validator <T : Any>(
        private val model: T,
        private val notifier: INotifier? = null
) {
    val constraintViolations = RulesViolations()

    val ruleBuilders: MutableList<IRuleBuilder<out Any>> = mutableListOf(
            AllLowerCaseRule,
            AllUpperCaseRule,
            AssertFalseRule,
            AssertTrueRule,
            AtLeastContainOneElementRule,
            AtLeastOneLowerCaseRule,
            AtLeastOneNumberCaseRule,
            AtLeastOneSpecialCharacterRule,
            AtLeastOneUpperCaseRule,
            ElementsNotEmptyRule,
            EmailRule,
            IntBetweenRule,
            IntLessThenRule,
            MinLenghtRule,
            NoNumbersRule,
            NotBlankRule,
            RegexRule,
            StartsWithRule
    )

    fun executeValidation():Boolean {
        model::class.declaredMemberProperties.forEachIndexed { index, kProperty ->

            val name = kProperty.name
            val annotations = kProperty.annotations

            if (annotations.isEmpty()) return@forEachIndexed

            val value = readProperty<Any?>(model, name)

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
                constraintViolations[name, value] = validationsViolated

            notifier?.run {
                if (validationsViolated.isEmpty()) {
                    validationSuccess(name, value, annotations)
                } else {
                    validationFail(name, value, validationsViolated)
                }

                if (notifier.stopValidation()) return false
            }
        }

        return constraintViolations.violations.isEmpty()
    }

    companion object {
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