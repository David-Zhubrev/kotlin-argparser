package com.appdav.argparser.exceptions

import kotlin.reflect.KClass

class ValueConversionException(input: String, targetValue: KClass<*>): Exception("Cannot convert value \"$input\" into instance of ${targetValue.qualifiedName}")
