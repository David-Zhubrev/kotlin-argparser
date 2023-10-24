package com.appdav.argparser.argument.options

import com.appdav.argparser.exceptions.NonInitializedValueException
import kotlin.reflect.KProperty

/**
 * RequiredOption is a non-nullable option which will make an ArgParser throw an RequiredArgumentMissingException if
 * `this` option is not present inside passed arguments
 */
abstract class RequiredOption<T : Any> : NullableOption<T>() {

    /**
     * Value container which will return parsed value or throw an exception if `this` argument has not been parsed
     * @throws NonInitializedValueException
     * @see NonInitializedValueException
     */
    final override val value: T
        @Throws(NonInitializedValueException::class)
        get() = super.value ?: throw NonInitializedValueException(this)

    final override fun getValue(thisRef: Any?, kProperty: KProperty<*>): T {
        return value
    }

    final override val required: Boolean = true

}
