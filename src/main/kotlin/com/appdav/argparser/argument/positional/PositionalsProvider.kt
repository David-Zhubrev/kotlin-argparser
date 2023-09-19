package com.appdav.argparser.argument.positional

import com.appdav.argparser.ArgRegistry
import com.appdav.argparser.ValueConverter
import com.appdav.argparser.argument.Validator

val ArgRegistry.Positionals: PositionalsProvider
    get() = PositionalsProviderImpl

interface PositionalsProvider {

    context(ArgRegistry)
    fun <T : Any> nullable(
        name: String,
        converter: ValueConverter<T>,
        position: Int = -1,
        description: String = name,
        validator: Validator<T> = { true },
    ): NullablePositional<T>

    context(ArgRegistry)
    fun <T : Any> required(
        name: String,
        converter: ValueConverter<T>,
        position: Int = -1,
        description: String = name,
        validator: Validator<T> = { true },
    ): RequiredPositional<T>

    context(ArgRegistry)
    fun <T : Any> withDefaultValue(
        name: String,
        converter: ValueConverter<T>,
        defaultValue: T,
        position: Int = -1,
        description: String = name,
        validator: Validator<T> = { true },
    ): PositionalWithDefaultValue<T>
}
