package com.appdav.argparser.argument.options

import com.appdav.argparser.ArgRegistry
import com.appdav.argparser.ValueConverter
import com.appdav.argparser.argument.Validator
import java.util.*


val ArgRegistry.Options: OptionsProvider
    get() = OptionsProviderImpl

interface OptionsProvider {

    context(ArgRegistry)
    fun <T : Any> required(
        token: String,
        converter: ValueConverter<T>,
        additionalTokens: List<String> = emptyList(),
        name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        description: String = name,
        validator: (T?) -> Boolean = { true },
    ): RequiredOption<T>

    context(ArgRegistry)
    fun <T : Any> nullable(
        token: String,
        converter: ValueConverter<T>,
        additionalTokens: List<String> = emptyList(),
        name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        description: String = name,
        validator: Validator<T> = { true },
    ): NullableOption<T>

    context(ArgRegistry)
    fun <T : Any> withDefaultValue(
        token: String,
        converter: ValueConverter<T>,
        defaultValue: T,
        name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        additionalTokens: List<String> = emptyList(),
        description: String = name,
        validator: Validator<T> = { true },
    ): OptionWithDefaultValue<T>

}
