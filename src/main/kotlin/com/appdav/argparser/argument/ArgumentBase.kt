package com.appdav.argparser.argument

import com.appdav.argparser.ValueConverter
import kotlin.reflect.KProperty

typealias Validator<T> = (value: T?) -> Boolean

abstract class ArgumentBase<T : Any> {

    open val value: T?
        get() = mValue

    open operator fun getValue(thisRef: Any, kProperty: KProperty<*>): T? = value

    abstract val name: String
    internal abstract val converter: ValueConverter<T>

    open val description: String
        get() = name

    internal open val validator: Validator<T> = { true }
    internal open val required: Boolean = false

    internal var isParsed: Boolean = false
        private set

    private var mValue: T? = null

    internal fun parseInput(input: String) {
        mValue = converter.convert(input)
        isParsed = true
    }

    internal fun validate(): Boolean {
        return validator(mValue)
    }
}
