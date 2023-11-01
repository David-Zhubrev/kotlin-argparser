package com.appdav.argparser.argument.flags

import com.appdav.argparser.argument.*
import com.appdav.argparser.argument.options.NullableOption
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.FlagConverter
import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.registries.FlagRegistryScope
import com.appdav.argparser.registries.RegistryBase
import kotlin.reflect.KProperty


/**
 * Flag is a simple argument denoted as single hyphen and a single letter. After parsing, it has a boolean value of true, if token is present in commandline arguments and false otherwise.
 * @see ArgumentBaseInternal
 */
abstract class Flag : ArgumentBaseInternal<Boolean>(), TokenizedArgument,
    DelegateArgument<Boolean>, DefaultValueArgument<Boolean> {

    override val converter: ValueConverter<Boolean> = DefaultConverters.FlagConverter

    /**
     * Value of this flag. Returns parsed value or defaultValue if it has not been parsed
     * @see defaultValue
     */
    final override val value: Boolean
        get() = super.value ?: defaultValue

    final override fun getValue(thisRef: Any?, kProperty: KProperty<*>): Boolean {
        return value
    }

    /**
     * Default value that is returned by value if `this` flag has not been initialized
     */
    final override val defaultValue: Boolean = false

    /**
     * All tokens associated with `this` flag
     * @return list of all tokens associated with `this` flag
     */
    final override fun allTokens(): List<String> = super.allTokens()

    final override val required: Boolean = false
    final override val validator: Validator<Boolean> = { true }
}
