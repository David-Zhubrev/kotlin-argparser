package com.appdav.argparser.argument.positional

import kotlin.reflect.KProperty

abstract class PositionalWithDefaultValue<T : Any> : NullablePositional<T>() {
    abstract val defaultValue: T
    final override val value: T
        get() = super.value ?: defaultValue

    final override fun getValue(thisRef: Any, kProperty: KProperty<*>): T {
        return value
    }

    final override val required: Boolean = false
}
