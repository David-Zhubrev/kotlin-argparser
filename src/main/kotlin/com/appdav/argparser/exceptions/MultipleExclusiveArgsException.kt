package com.appdav.argparser.exceptions

class MultipleExclusiveArgsException(
    token1: String,
    token2: String
): IllegalArgumentException("Arguments $token1 and $token2 are mutually exlusive, which means they cannot be used together. Please, choose one")
