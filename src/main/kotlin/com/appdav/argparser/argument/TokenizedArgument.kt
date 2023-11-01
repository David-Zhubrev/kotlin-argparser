package com.appdav.argparser.argument

/**
 * Interface that marks an argument as a tokenized, which means it has command-line tokens
 */
interface TokenizedArgument {

    /**
     * Command-line token
     */
    val token: String

    /**
     * Additional command-line tokens
     */

    val additionalTokens: List<String>
        get() = emptyList()

    /**
     * get all tokens of `this` tokenized argument
     * @return all tokens
     */
    fun allTokens(): List<String> = additionalTokens + token


}
