package com.appdav.argparser.exceptions

class TooManyPositionalArgumentsException(args: List<String>) :
    IllegalArgumentException("Too many positional arguments found: ${args.joinToString(" ")}")
