package com.appdav.argparser.argument.options

import com.appdav.argparser.argument.DefaultValueArgument
import kotlin.reflect.KProperty

/**
 * Option that has default value, which will be returned in case `this` option has not been parsed, which makes `this` non-nullable type
 */
abstract class OptionWithDefaultValue<T : Any> : NullableOption<T>(),
    DefaultValueArgument<T> {

    /**
     * Default value of `this` option
     */
    abstract override val defaultValue: T


    /**
     * Value container for `this` option. Will return parsed value or default value, if `this` option has not been parsed
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
