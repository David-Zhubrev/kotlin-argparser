package com.appdav.argparser.argument

import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.exceptions.ValueConversionException
import kotlin.reflect.KProperty

/**
 * Validation function provided for argument in order to validate value after the parsing process.
 * It can be useful when you want to make user use only specific values, for instance, when you want to make user use only positive integers etc.
 */
typealias Validator<T> = (value: T?) -> Boolean


/**
 * It is a base class for any other types of arguments, like options, flags and positional arguments.
 * If you don't want to make your own ArgParser implementation, please DO NOT use this class.
 */
abstract class ArgumentBaseInternal<T : Any> {


    /**
     * Value container for `this` argument, where parsed value will be put after the parsing process.
     * Note that if accessed BEFORE calling ArgParser.parse() it can return null, defaultValue(if available) or throw an NonInitializedValueAccessException
     * @see com.appdav.argparser.exceptions.NonInitializedValueException
     */
    open val value: T?
        get() = mValue

    /**
     * Delegate function for returning `this` value using "by" syntax
     * @see value
     */
//    open operator fun getValue(thisRef: Any?, kProperty: KProperty<*>): T? = value

    /**
     * Name of `this` argument
     */
    abstract val name: String

    /**
     * Value converter that is used during parsing process
     * @see ValueConverter
     */
    internal abstract val converter: ValueConverter<T>

    /**
     * Description of `this` argument used by help functions etc. Same as the name by default
     * @see name
     */
    open val description: String
        get() = name

    /**
     * Validator function that is used to validate value of `this` argument AFTER parsing. Just returns true by default.
     * @see Validator
     */
    internal open val validator: Validator<T> = { true }

    /**
     * Denotes `this` argument as required, which makes ArgParser throw an RequiredArgumentMissingException if `this` argument is missing in passed arguments.
     * @see com.appdav.argparser.exceptions.RequiredArgumentMissingException
     */
    internal open val required: Boolean = false

    /**
     * Flag that denotes whether `this` argument has been parsed
     */
    internal var isParsed: Boolean = false
        private set

    /**
     * Internal value container
     */
    private var mValue: T? = null

    /**
     * Called by parser in order to convert string input into `this` argument's value type.
     */

    internal fun parseInput(input: String) {
        mValue = converter.convert(input)
        isParsed = true
    }

    /**
     * Calls the validation function
     */
    internal fun validate(): Boolean {
        return validator(mValue)
    }

    internal fun reset() {
        this.mValue = null
    }

    override fun toString(): String {
        return value.toString()
    }
}
