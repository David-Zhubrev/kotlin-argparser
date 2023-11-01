package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.ArgumentBaseInternal

class NoValueForOptionException(option: ArgumentBaseInternal<*>) :
    IllegalArgumentException("No value provided for option: ${option.name}")
