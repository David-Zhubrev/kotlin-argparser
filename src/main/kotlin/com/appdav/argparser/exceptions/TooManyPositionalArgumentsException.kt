package com.appdav.argparser.exceptions

/**
 * Occurs when positional argument count is less than the count of positional arguments inside provided input array
 */
class TooManyPositionalArgumentsException(args: List<String>) :
    IllegalArgumentException("Too many positional arguments found: ${args.joinToString(" ")}")
