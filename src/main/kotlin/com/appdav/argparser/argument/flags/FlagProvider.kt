package com.appdav.argparser.argument.flags

import com.appdav.argparser.argument.ArgumentProviderDsl
import com.appdav.argparser.registries.FlagRegistryScope

@ArgumentProviderDsl
val FlagRegistryScope.Flags: FlagProvider
    get() = FlagProviderImpl

interface FlagProvider{

    /**
     * Creates instance of Flag with specified parameters and registers it inside `this` FlagRegistryScope
     * @param token main token of created option
     * @param name name for created option
     * @param additionalTokens additional tokens associated with created option
     * @param description description of created option used by help functions etc.
     * @see Flag
     * @see FlagRegistryScope
     */
    context(FlagRegistryScope)
    fun flag(
        token: String,
        name: String,
        additionalTokens: List<String> = emptyList(),
        description: String = name,
    ): Flag


    /**
     * Creates instance of InvertedFlag with specified parameters and registers it inside `this` FlagRegistryScope
     * @param token main token of created option
     * @param name name for created option
     * @param additionalTokens additional tokens associated with created option
     * @param description description of created option used by help functions etc.
     * @see InvertedFlag
     * @see FlagRegistryScope
     */
    context(FlagRegistryScope)
    fun invertedFlag(
        token: String,
        name: String,
        additionalTokens: List<String> = emptyList(),
        description: String = name,
    ): InvertedFlag




}
