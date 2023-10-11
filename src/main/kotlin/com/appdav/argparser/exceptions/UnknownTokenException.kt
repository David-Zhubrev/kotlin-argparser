package com.appdav.argparser.exceptions

class UnknownTokenException(unknownTokens: Collection<String>) :
    IllegalArgumentException("Unknown arguments: ${unknownTokens.joinToString("\n") { it }}")
