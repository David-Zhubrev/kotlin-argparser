package com.appdav.argparser.argument.positional

import kotlin.reflect.KProperty

abstract class RequiredPositional<T : Any> : NullablePositional<T>() {
    final override val value: T
        get() = super.value
            ?: throw IllegalStateException("Trying to access value of argument which has not been parsed")

    final override fun getValue(thisRef: Any, kProperty: KProperty<*>): T {
        return value
    }

    final override val required: Boolean = true
}
