package com.appdav.argparser.argument.flags

import com.appdav.argparser.argument.ArgumentProviderDsl
import com.appdav.argparser.converter.ValueConverter
import com.appdav.argparser.registries.FlagRegistryScope

/**
 * Creates instance of Flag with specified parameters and registers it inside `this` FlagRegistryScope
 * @param token main token of created option
 * @param name name for created option
 * @param additionalTokens additional tokens associated with created option
 * @param description description of created option used by help functions etc.
 * @param defaultValue default value for the flag, which is applied when flag is not present in input. False by default
 * @return newly created and registered RequiredOption
 * @see Flag
 * @see FlagRegistryScope
 * @see Validator
 * @see ValueConverter
 */
@ArgumentProviderDsl
fun FlagRegistryScope.flag(
    token: String,
    name: String,
    additionalTokens: List<String> = emptyList(),
    description: String = name
): Flag {
    val flag = object : Flag() {
        override val name: String = name
        override val token: String = token
        override val additionalTokens: List<String> = additionalTokens
        override val description: String = description
    }
    registerFlag(flag)
    return flag
}
