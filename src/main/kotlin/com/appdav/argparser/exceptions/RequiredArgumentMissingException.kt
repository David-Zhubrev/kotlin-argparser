package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.ArgumentBaseInternal

/**
 * Thrown when required argument is not parsed because command-line input lacks the specified argument
 */
class RequiredArgumentMissingException(argument: ArgumentBaseInternal<*>) : IllegalStateException(
    "Required argument ${argument.name} is not found in command-line input"
)
