package com.appdav.argparser.exceptions

/**
 * This exception occurs when more than one argument from the exclusive groups are initialized
 */
class MultipleExclusiveArgsException(
    token1: String,
    token2: String
): IllegalArgumentException("Arguments $token1 and $token2 are mutually exclusive, which means they cannot be used together")
