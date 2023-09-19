package com.appdav.argparser.argument.options

import kotlin.reflect.KProperty

abstract class RequiredOption<T : Any> : NullableOption<T>() {

    final override val value: T
        get() = super.value ?: throw IllegalStateException("Trying to access value which has not been parsed")

    final override fun getValue(thisRef: Any, kProperty: KProperty<*>): T {
        return value
    }

    final override val required: Boolean = true

}
