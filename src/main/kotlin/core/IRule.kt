package org.example.core


interface IRule <T : Any> {
    fun isValid(value: T?): Boolean
}