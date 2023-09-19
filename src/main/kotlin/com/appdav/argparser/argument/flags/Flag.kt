package com.appdav.argparser.argument.flags

import com.appdav.argparser.ArgRegistry
import com.appdav.argparser.BooleanConverter
import com.appdav.argparser.ValueConverter
import com.appdav.argparser.argument.ArgumentBase
import com.appdav.argparser.argument.Validator
import java.util.*
import kotlin.reflect.KProperty


//TODO: reserve help flag and throw an exception if it is used
abstract class Flag : ArgumentBase<Boolean>() {

    override val converter: ValueConverter<Boolean> = BooleanConverter //TODO: replace with separate flag converter

    final override val value: Boolean
        get() = super.value ?: defaultValue

    final override fun getValue(thisRef: Any, kProperty: KProperty<*>): Boolean {
        return value
    }

    abstract val token: String
    open val additionalTokens: List<String> = emptyList()

    open val defaultValue: Boolean = false

    internal fun allTokens(): List<String> = additionalTokens + token

    final override val required: Boolean = false
    final override val validator: Validator<Boolean> = { true }
}

//TODO: move into registry??
fun ArgRegistry.flag(
    token: String,
    additionalTokens: List<String> = emptyList(),
    name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    description: String = name,
): Flag {
    val flag = object : Flag() {
        override val name: String = name
        override val token: String = token
        override val additionalTokens: List<String> = additionalTokens
        override val description: String = description
    }
    registerArgument(flag)
    return flag
}
