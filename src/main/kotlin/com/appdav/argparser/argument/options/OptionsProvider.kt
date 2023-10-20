package com.appdav.argparser.argument.options

import com.appdav.argparser.registries.RegistryBase
import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.argument.Validator
import java.util.*


val RegistryBase.Options: OptionsProvider
    get() = OptionsProviderImpl

/**
 * Specifies the way that options can be created inside ArgParser instance
 */
interface OptionsProvider {

    /**
     * Creates instance of RequiredOption with specified parameters and registers it inside `this` ArgRegistry
     * @param token main token of created option
     * @param converter ValueConverter that will be used to convert string input into option's value
     * @param additionalTokens additional tokens associated with created option
     * @param name name for created option
     * @param description description of created option used by help functions etc.
     * @param validator validation function called after parsing
     * @return newly created and registered RequiredOption
     * @see RequiredOption
     * @see RegistryBase
     * @see Validator
     * @see ValueConverter
     */
    context(RegistryBase)
    fun <T : Any> required(
        token: String,
        converter: ValueConverter<T>,
        additionalTokens: List<String> = emptyList(),
        name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        description: String = name,
        validator: (T?) -> Boolean = { true },
    ): RequiredOption<T>


    /**
     * Creates instance of NullableOption with specified parameters and registers it inside `this` ArgRegistry
     * @param token main token of created option
     * @param converter ValueConverter that will be used to convert string input into option's value
     * @param additionalTokens additional tokens associated with created option
     * @param name name for created option
     * @param description description of created option used by help functions etc.
     * @param validator validation function called after parsing
     * @return newly created and registered RequiredOption
     * @see NullableOption
     * @see RegistryBase
     * @see Validator
     * @see ValueConverter
     */
    context(RegistryBase)
    fun <T : Any> nullable(
        token: String,
        converter: ValueConverter<T>,
        additionalTokens: List<String> = emptyList(),
        name: String = token.removePrefix("--").removePrefix("-").replace("-", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        description: String = name,
        validator: Validator<T> = { true },
    ): NullableOption<T>


    /**
     * Creates instance of RequiredOption with specified parameters and registers it inside `this` ArgRegistry
     * @param token main token of created option
     * @param converter ValueConverter that will be used to convert string input into option's value
     * @param defaultValue Default value that will be returned by value container if created option is not parsed
     * @param additionalTokens additional tokens associated with created option
     * @param name name for created option
     * @param description description of created option used by help functions etc.
     * @param validator validation function called after parsing
     * @return newly created and registered RequiredOption
     * @see OptionWithDefaultValue
     * @see RegistryBase
     * @see Validator
     * @see ValueConverter
     */
    context(RegistryBase)
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
