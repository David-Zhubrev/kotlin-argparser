package com.appdav.argparser.argument.positional

import kotlin.reflect.KProperty

/**
 * RequiredPositional is non-nullable positional argument. If it was not presented in command-line arguments, the RequiredArgumentMissingException will be thrown
 * @see NullablePositional
 * @see com.appdav.argparser.exceptions.RequiredArgumentMissingException
 */
abstract class RequiredPositional<T : Any> : NullablePositional<T>() {

    /**
     * Value container, which returns the parsed value or throws NonInitializedValueException if accessed BEFORE parsing
     * @see com.appdav.argparser.exceptions.NonInitializedValueException
     */
    final override val value: T
        get() = super.value
            ?: throw IllegalStateException("Trying to access value of argument which has not been parsed")

    final override fun getValue(thisRef: Any?, kProperty: KProperty<*>): T {
        return value
    }

    final override val required: Boolean = true
}
