package com.appdav.argparser.exceptions

import kotlin.reflect.KClass

/**
 * Exception thrown by ValueConverter which means the string input cannot be converted into instance of type specified by the ValueConverter
 * @see com.appdav.argparser.converter.ValueConverter
 */
class ValueConversionException(input: String, targetValue: KClass<*>, reason: String? = null) :
    Exception("Cannot convert value \"$input\" into instance of ${targetValue.qualifiedName}".let { if (reason != null) "$it\nReason: $reason" else it})
