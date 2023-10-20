package com.appdav.argparser.argument.options

import com.appdav.argparser.registries.RegistryBase
import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.argument.Validator


/**
 * Internal implementation of OptionsProvider
 */
internal object OptionsProviderImpl : OptionsProvider {
    context(RegistryBase)
    override fun <T : Any> required(
        token: String,
        converter: ValueConverter<T>,
        additionalTokens: List<String>,
        name: String,
        description: String,
        validator: (T?) -> Boolean,
    ): RequiredOption<T> {
        val arg = object : RequiredOption<T>() {
            override val converter: ValueConverter<T> = converter
            override val token: String = token
            override val additionalTokens: List<String> = additionalTokens
            override val description: String = description
            override val validator: (T?) -> Boolean = validator
        }
        registerArgument(arg)
        return arg
    }


    context(RegistryBase)
    override fun <T : Any> nullable(
        token: String,
        converter: ValueConverter<T>,
        additionalTokens: List<String>,
        name: String,
        description: String,
        validator: Validator<T>,
    ): NullableOption<T> {
        val arg = object : NullableOption<T>() {
            override val converter: ValueConverter<T> = converter
            override val token: String = token
            override val additionalTokens: List<String> = additionalTokens
            override val name: String = name
            override val description: String = description
            override val validator: Validator<T> = validator
        }
        registerArgument(arg)
        return arg
    }

    context(RegistryBase)
    override fun <T : Any> withDefaultValue(
        token: String,
        converter: ValueConverter<T>,
        defaultValue: T,
        name: String,
        additionalTokens: List<String>,
        description: String,
        validator: Validator<T>,
    ): OptionWithDefaultValue<T> {
        val arg = object : OptionWithDefaultValue<T>() {
            override val converter: ValueConverter<T> = converter
            override val token: String = token
            override val defaultValue: T = defaultValue
            override val additionalTokens: List<String> = additionalTokens
            override val name: String = name
            override val description: String = description
            override val validator: Validator<T> = validator
        }
        registerArgument(arg)
        return arg
    }

}
