package com.appdav.argparser.argument.flags

import com.appdav.argparser.registries.FlagRegistryScope

internal object FlagProviderImpl : FlagProvider {

    context(FlagRegistryScope)
    override fun flag(
        token: String,
        name: String,
        additionalTokens: List<String>,
        description: String,
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

    context(FlagRegistryScope)
    override fun invertedFlag(
        token: String,
        name: String,
        additionalTokens: List<String>,
        description: String,
    ): InvertedFlag {
        val invertedFlag = object : InvertedFlag() {
            override val name: String = name
            override val token: String = token
            override val additionalTokens: List<String> = additionalTokens
            override val description: String = description
        }
        registerInvertedFlag(invertedFlag)
        return invertedFlag
    }
}
