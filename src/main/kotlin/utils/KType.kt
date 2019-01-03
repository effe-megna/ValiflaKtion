package org.example.utils

import java.lang.reflect.*
import kotlin.reflect.*
import kotlin.reflect.full.createType

// --- Interface ---

inline fun <reified T : Any> getKType(): KType =
        object : SuperTypeTokenHolder<T>() {}.getKTypeImpl()

// --- Implementation ---

@Suppress("unused")
open class SuperTypeTokenHolder<T>

fun SuperTypeTokenHolder<*>.getKTypeImpl(): KType =
        javaClass.genericSuperclass.toKType().arguments.single().type!!

fun KClass<*>.toInvariantFlexibleProjection(arguments: List<KTypeProjection> = emptyList()): KTypeProjection {
    // TODO: there should be an API in kotlin-reflect which creates KType instances corresponding to flexible types
    // Currently we always produce a non-null type, which is obviously wrong
    return KTypeProjection.invariant(createType(arguments, nullable = false))
}

fun Type.toKTypeProjection(): KTypeProjection = when (this) {
    is Class<*> -> this.kotlin.toInvariantFlexibleProjection(if(this.isArray) listOf(this.componentType.toKTypeProjection()) else emptyList())
    is ParameterizedType -> {
        val erasure = (rawType as Class<*>).kotlin
        erasure.toInvariantFlexibleProjection((erasure.typeParameters.zip(actualTypeArguments).map { (parameter, argument) ->
            val projection = argument.toKTypeProjection()
            projection.takeIf {
                // Get rid of use-site projections on arguments, where the corresponding parameters already have a declaration-site projection
                parameter.variance == KVariance.INVARIANT || parameter.variance != projection.variance
            } ?: KTypeProjection.invariant(projection.type!!)
        }))
    }
    is WildcardType -> when {
        lowerBounds.isNotEmpty() -> KTypeProjection.contravariant(lowerBounds.single().toKType())
        upperBounds.isNotEmpty() -> KTypeProjection.covariant(upperBounds.single().toKType())
        // This looks impossible to obtain through Java reflection API, but someone may construct and pass such an instance here anyway
        else -> KTypeProjection.STAR
    }
    is GenericArrayType -> Array<Any>::class.toInvariantFlexibleProjection(listOf(genericComponentType.toKTypeProjection()))
    is TypeVariable<*> -> TODO() // TODO
    else -> throw IllegalArgumentException("Unsupported type: $this")
}

fun Type.toKType(): KType = toKTypeProjection().type!!