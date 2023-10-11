package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.ArgumentBaseInternal

/**
 * Thrown when trying to access value of the non-nullable argument which has not been initialized.
 * It is basically thrown by the required arguments, so if it is caught, it basically means that the end user is trying to access value of argument BEFORE the ArgParser.parse() is called
 */
class NonInitializedValueException
    (argument: ArgumentBaseInternal<*>) :
    IllegalStateException("Trying to access value of non-initialized argument ${argument.name}")
