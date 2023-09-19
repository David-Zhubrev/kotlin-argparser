package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.ArgumentBaseInternal

class NonInitializedValueException(argument: ArgumentBaseInternal<*>) :
        IllegalStateException("Trying to access value of non-initialized argument ${argument.name}")
