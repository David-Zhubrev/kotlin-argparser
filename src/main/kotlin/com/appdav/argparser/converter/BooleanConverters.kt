package com.appdav.argparser.converter

import com.appdav.argparser.exceptions.ValueConversionException

/**
 * Special converter for flag arguments, which returns true on blank input or throws ValueConversionException otherwise
 * @see ValueConversionException
 * @see ValueConverter
 */
internal val DefaultConverters.FlagConverter: ValueConverter<Boolean>
    get() = ValueConverter { input ->
        if (input.isBlank()) true
        else throw ValueConversionException(input, Boolean::class)
    }

/**
 * Special converter for flag arguments, which returns false on blank input or throws ValueConversionException otherwise
 * @see ValueConversionException
 * @see ValueConverter
 */
internal val DefaultConverters.InvertedFlagConverter: ValueConverter<Boolean>
    get() = ValueConverter{input ->
        if (input.isBlank()) false
        else throw ValueConversionException(input, Boolean::class)
    }

/**
 * Converter which returns true on y, yes and true; false on n, no and false, or throws `ValueConversionException` otherwise
 * @see ValueConversionException
 * @see ValueConverter
 */
val DefaultConverters.BooleanConverter: ValueConverter<Boolean>
    get() = ValueConverter { input ->
        when (input) {
            "y", "yes", "true" -> true
            "n", "no", "false" -> false
            else -> throw ValueConversionException(input, Boolean::class)
        }
    }

/**
 * Creates converter for boolean types, which returns true on y, yes and true; returns false on n, no and false.
 * If input is blank, it returns value passed as function parameter if it is not null, otherwise throws an ValueConversionException
 * @param valueOnEmptyInput value which will be returned by created converter if input is blank. If null, converter will throw ValueConversionException on empty input
 * @return ValueConverter<Boolean>
 * @see ValueConverter
 * @see ValueConversionException
 */
fun DefaultConverters.BooleanConverter(valueOnEmptyInput: Boolean? = null) =
    ValueConverter { input ->
        if (input.isBlank() && valueOnEmptyInput != null) valueOnEmptyInput
        else when (input) {
            "y", "yes", "true" -> true
            "n", "no", "false" -> false
            else -> throw ValueConversionException(input, Boolean::class)
        }
    }
