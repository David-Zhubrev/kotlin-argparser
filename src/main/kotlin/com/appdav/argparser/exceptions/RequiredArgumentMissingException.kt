package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.ArgumentBaseInternal

class RequiredArgumentMissingException(argument: ArgumentBaseInternal<*>): IllegalStateException(
    "Required argument is not present"
)
