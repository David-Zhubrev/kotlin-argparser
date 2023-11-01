package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.TokenizedArgument

/**
 * This exception occurs when input contains more than one token associated with a single tokenized argument
 */
class ArgumentMultipleOccurrenceException
/**
 * @param argumentName name of the argument
 * @param arg tokenized argument instance
 * @param input string array that was initially passed into ArgParser instance
 */
    (
    argumentName: String,
    arg: TokenizedArgument,
    input: Array<String>,
) : IllegalArgumentException(
    "Argument \"$argumentName\" with tokens ${
        arg.allTokens().joinToString(", ", "[", "]")
    } occurred more than once in program's input: ${input.joinToString(" ", "\"", "\"")}"
)
