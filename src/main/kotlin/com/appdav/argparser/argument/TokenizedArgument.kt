package com.appdav.argparser.argument

interface TokenizedArgument {

    val token: String

    val additionalTokens: List<String>

    fun allTokens(): List<String>

}
