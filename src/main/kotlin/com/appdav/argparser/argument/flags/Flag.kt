package com.appdav.argparser.argument.flags

import com.appdav.argparser.argument.*
import com.appdav.argparser.converter.DefaultConverters
import com.appdav.argparser.converter.FlagConverter
import com.appdav.argparser.converter.InvertedFlagConverter
import com.appdav.argparser.converter.ValueConverter
import kotlin.reflect.KProperty

/**
 * Flag is a simple argument that is denoted by hyphen (single or double) and has no value provided afterward. FlagBaseInternal has 2 implementations: Flag and InvertedFlag
 * @see Flag
 * @see InvertedFlag
 * @see ArgumentBaseInternal
 */
sealed class FlagBaseInternal : ArgumentBaseInternal<Boolean>(),
    TokenizedArgument,
    DelegateArgument<Boolean>,
    DefaultValueArgument<Boolean> {

    abstract override val converter: ValueConverter<Boolean>

    final override val value: Boolean
        get() = super.value ?: defaultValue

    final override fun getValue(thisRef: Any?, kProperty: KProperty<*>): Boolean {
        return value
    }

    abstract override val defaultValue: Boolean

    final override fun allTokens(): List<String> = super.allTokens()
    final override val required: Boolean = false
    final override val validator: Validator<Boolean> = { true }


}

/**
 * Flag is a simple argument that is denoted by hyphen (single or double) and has no value provided afterward. It is false by default, true if found in command line input
 * @see FlagBaseInternal
 */
abstract class Flag : FlagBaseInternal() {
    final override val converter = DefaultConverters.FlagConverter
    final override val defaultValue = false
}

/**
 * Inverted flag is a simple argument that is denoted by hyphen (single or double) and has no value provided afterward. On the opposite of Flag, it is true by default, false if found in command line input
 * @see FlagBaseInternal
 * @see Flag
 */
abstract class InvertedFlag : FlagBaseInternal() {
    final override val converter = DefaultConverters.InvertedFlagConverter
    final override val defaultValue = true
}
