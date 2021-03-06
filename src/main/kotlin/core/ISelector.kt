package org.example.core

import kotlin.reflect.KClass

interface ISelector<T, R> {
    fun extractValueToValidate(value: T?): R?
}

@Target(AnnotationTarget.PROPERTY)
annotation class Selector(val selector: KClass<*>)