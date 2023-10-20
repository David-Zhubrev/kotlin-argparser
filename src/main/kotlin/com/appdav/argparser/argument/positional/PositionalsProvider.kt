package com.appdav.argparser.argument.positional

import com.appdav.argparser.registries.RegistryBase
import com.appdav.argparser.argument.Validator
import com.appdav.argparser.converter.ValueConverter

/**
 * @see PositionalsProvider
 */
val RegistryBase.Positionals: PositionalsProvider
    get() = PositionalsProviderImpl

/**
 * Specifies the way that positional arguments can be created inside ArgParser instance
 */
interface PositionalsProvider {

    /**
     * Creates instance of NullablePositional with specified parameters and registers it inside `this` ArgRegistry
     * @param name name for created option
     * @param converter ValueConverter that will be used to convert string input into option's value
     * @param position position used to sort positional arguments within `this` ArgRegistry
     * @param description description of created option used by help functions etc.
     * @param validator validation function called after parsing
     * @return newly created and registered RequiredOption
     * @see NullablePositional
     * @see NullablePositional.position
     * @see RegistryBase
     * @see Validator
     * @see ValueConverter
     */
    context(RegistryBase)
    fun <T : Any> nullable(
        name: String,
        converter: ValueConverter<T>,
        position: Int = -1,
        description: String = name,
        validator: Validator<T> = { true },
    ): NullablePositional<T>

    /**
     * Creates instance of RequiredPositional with specified parameters and registers it inside `this` ArgRegistry
     * @param name name for created option
     * @param converter ValueConverter that will be used to convert string input into option's value
     * @param position position used to sort positional arguments within `this` ArgRegistry
     * @param description description of created option used by help functions etc.
     * @param validator validation function called after parsing
     * @return newly created and registered RequiredOption
     * @see RequiredPositional
     * @see RequiredPositional.position
     * @see RegistryBase
     * @see Validator
     * @see ValueConverter
     */
    context(RegistryBase)
    fun <T : Any> required(
        name: String,
        converter: ValueConverter<T>,
        position: Int = -1,
        description: String = name,
        validator: Validator<T> = { true },
    ): RequiredPositional<T>

    /**
     * Creates instance of PositionalWithDefaultValue with specified parameters and registers it inside `this` ArgRegistry
     * @param name name for created option
     * @param converter ValueConverter that will be used to convert string input into option's value
     * @param position position used to sort positional arguments within `this` ArgRegistry
     * @param description description of created option used by help functions etc.
     * @param validator validation function called after parsing
     * @return newly created and registered RequiredOption
     * @see PositionalWithDefaultValue
     * @see PositionalWithDefaultValue.position
     * @see RegistryBase
     * @see Validator
     * @see ValueConverter
     */
    context(RegistryBase)
    fun <T : Any> withDefaultValue(
        name: String,
        converter: ValueConverter<T>,
        defaultValue: T,
        position: Int = -1,
        description: String = name,
        validator: Validator<T> = { true },
    ): PositionalWithDefaultValue<T>
}
