package com.appdav.argparser.argument.options

import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.argument.Validator
import com.appdav.argparser.registries.OptionRegistryScope


/**
 * Internal implementation of OptionsProvider
 */
internal object OptionsProviderImpl : OptionsProvider {
    context(OptionRegistryScope)
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
        registerOption(arg)
        return arg
    }


    context(OptionRegistryScope)
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
        registerOption(arg)
        return arg
    }

    context(OptionRegistryScope)
    override fun <T : Any> withDefaultValue(
        token: String,
        converter: ValueConverter<T>,
        defaultValue: T,
        name: String,
        additionalTokens: List<String>,
        description: String,
        validator: Validator<T>,
        defaultValueToStringConverter: (T) -> String
    ): OptionWithDefaultValue<T> {
        val arg = object : OptionWithDefaultValue<T>() {
            override val converter: ValueConverter<T> = converter
            override val token: String = token
            override val defaultValue: T = defaultValue
            override val additionalTokens: List<String> = additionalTokens
            override val name: String = name
            override val description: String = description
            override val validator: Validator<T> = validator
            override fun createDefaultValueHelpString(defaultValue: T): String {
                return defaultValueToStringConverter(defaultValue)
            }
        }
        registerOption(arg)
        return arg
    }

}
