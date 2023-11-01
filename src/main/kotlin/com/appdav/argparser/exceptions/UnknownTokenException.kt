package com.appdav.argparser.exceptions

/**
 * Occurs when input contains token that is not associated with any registered argument and parser is not in the ignoreUnknownTokens mode
 */
class UnknownTokenException(unknownTokens: Collection<String>) :
    IllegalArgumentException("Unknown arguments: ${unknownTokens.joinToString("\n") { it }}")
