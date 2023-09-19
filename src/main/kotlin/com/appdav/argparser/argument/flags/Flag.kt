package com.appdav.argparser.argument.flags

import com.appdav.argparser.ArgRegistry
import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.argument.ArgumentBaseInternal
import com.appdav.argparser.argument.Validator
import com.appdav.argparser.converter.FlagConverter
import com.appdav.argparser.converter.DefaultConverters
import java.util.*
import kotlin.reflect.KProperty


/**
 * Flag is a simple argument denoted as single hyphen and a single letter. After parsing, it has a boolean value of true, if token is present in commandline arguments and false otherwise.
 * @see ArgumentBaseInternal
 */
abstract class Flag : ArgumentBaseInternal<Boolean>() {

    override val converter: ValueConverter<Boolean> = DefaultConverters.FlagConverter

    /**
     * Value of this flag. Returns parsed value or defaultValue if it has not been parsed
     * @see defaultValue
     */
    final override val value: Boolean
        get() = super.value ?: defaultValue

    final override fun getValue(thisRef: Any, kProperty: KProperty<*>): Boolean {
        return value
    }

    /**
     * Token associated with `this` flag
     */
    abstract val token: String

    /**
     * Additional tokens associated with `this` flag.
     */
    open val additionalTokens: List<String> = emptyList()

    /**
     * Default value that is returned by value if `this` flag has not been initialized
     */
    open val defaultValue: Boolean = false

    /**
     * All tokens associated with `this` flag
     * @return list of all tokens associated with `this` flag
     */
    internal fun allTokens(): List<String> = additionalTokens + token

    final override val required: Boolean = false
    final override val validator: Validator<Boolean> = { true }
}


fun ArgRegistry.flag(
    token: String,
    additionalTokens: List<String> = emptyList(),
    name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    description: String = name,
    defaultValue: Boolean = false
): Flag {
    val flag = object : Flag() {
        override val name: String = name
        override val token: String = token
        override val additionalTokens: List<String> = additionalTokens
        override val defaultValue: Boolean = defaultValue
        override val description: String = description
    }
    registerArgument(flag)
    return flag
}