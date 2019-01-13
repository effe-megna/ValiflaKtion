package rules

import org.example.core.IRule
import org.example.core.IRuleBuilder

class StartsWithRule(
        val target: String
) : IRule<String> {

    @Target(AnnotationTarget.PROPERTY)
    annotation class StartsWith(val target: String)

    override fun isValid(value: String?): Boolean {
        return value?.startsWith(target) ?: false
    }

    companion object Builder : IRuleBuilder<String> {
        override fun buildFromAnnotation(annotation: Annotation): IRule<String>? {
            return if (annotation is StartsWith) {
                StartsWithRule(annotation.target)
            } else null
        }
    }
}