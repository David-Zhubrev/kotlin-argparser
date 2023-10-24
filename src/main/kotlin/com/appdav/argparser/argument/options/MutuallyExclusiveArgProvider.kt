package com.appdav.argparser.argument.options

import com.appdav.argparser.argument.ArgumentProviderDsl
import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.registries.MutuallyExclusiveGroupScope
import java.util.*

@ArgumentProviderDsl
fun <T : Any> MutuallyExclusiveGroupScope.oneOf(
    token: String,
    converter: ValueConverter<T>,
    additionalTokens: List<String> = emptyList(),
    name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    description: String = name,
    validator: (T?) -> Boolean = { true },
): RequiredOption<T> {
    val arg = object : RequiredOption<T>() {
        override val name: String = name
        override val converter: ValueConverter<T> = converter
        override val token: String = token
        override val additionalTokens: List<String> = additionalTokens
        override val description: String = description
        override val validator: (T?) -> Boolean = validator
    }
    registerOneOf(arg)
    return arg
}
