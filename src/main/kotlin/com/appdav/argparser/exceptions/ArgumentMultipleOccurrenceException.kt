package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.TokenizedArgument

class ArgumentMultipleOccurrenceException(
    argumentName: String,
    arg: TokenizedArgument,
    input: Array<String>,
) : IllegalArgumentException(
    "Argument \"$argumentName\" with tokens ${
        arg.allTokens().joinToString(", ", "[", "]")
    } occurred more than once in program's input: ${input.joinToString(" ", "\"", "\"")}"
)
