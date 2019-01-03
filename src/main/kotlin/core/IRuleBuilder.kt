package org.example.core

import kotlin.reflect.KType

interface IRuleBuilder <T : Any> {
    val kType: KType
    fun buildFromAnnotation(annotation: Annotation): IRule<T>?
}