package org.example.core

interface IRuleBuilder <T : Any> {
    fun buildFromAnnotation(annotation: Annotation): IRule<T>?
}