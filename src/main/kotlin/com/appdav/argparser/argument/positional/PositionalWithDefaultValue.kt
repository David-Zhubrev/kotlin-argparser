package com.appdav.argparser.argument.positional

import com.appdav.argparser.argument.DefaultValueArgument
import kotlin.reflect.KProperty

/**
 * Positional argument that has a default value, which makes it non-nullable. If it was not provided or parsed, it will contain the default value
 */
abstract class PositionalWithDefaultValue<T : Any> : NullablePositional<T>(),
    DefaultValueArgument<T> {

    abstract override val defaultValue: T

    /**
     * Value container that will return a parsed value or default value if it was not parsed
     */
    final override val value: T
        get() = super.value ?: defaultValue

    final override fun getValue(thisRef: Any?, kProperty: KProperty<*>): T {
        return value
    }

    final override val required: Boolean = false

    final override val defaultValueString: String
        get() = super.defaultValueString

}
