package com.appdav.argparser.argument.options

import kotlin.reflect.KProperty

abstract class OptionWithDefaultValue<T : Any> : NullableOption<T>() {

    protected abstract val defaultValue: T

    final override val value: T
        get() = super.value ?: defaultValue

    final override fun getValue(thisRef: Any, kProperty: KProperty<*>): T {
        return value
    }

    final override val required: Boolean = false

}
