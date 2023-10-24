package com.appdav.argparser.exceptions

import com.appdav.argparser.argument.options.NullableOption

class NoValueForOptionException(option: NullableOption<*>) :
    IllegalArgumentException("No value provided for option: ${option.name}")
