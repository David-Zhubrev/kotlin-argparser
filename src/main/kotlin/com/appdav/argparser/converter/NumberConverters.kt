package com.appdav.argparser.converter

import com.appdav.argparser.exceptions.ValueConversionException


/**
 * Creates String-to-Byte converter
 * @param returnZeroIfCantConvert if true, returns 0 if conversion is unsuccessful, if false throws a ValueConversionException
 * @return ValueConverter<Byte>
 */
fun DefaultConverters.ByteConverter(
    returnZeroIfCantConvert: Boolean = false,
): ValueConverter<Byte> =
    ValueConverter {
        it.toByteOrNull() ?: if (returnZeroIfCantConvert) 0 else throw ValueConversionException(it, Byte::class)
    }


/**
 * Creates String-to-Short converter
 * @param returnZeroIfCantConvert if true, returns 0 if conversion is unsuccessful, if false throws a ValueConversionException
 * @return ValueConverter<Short>
 */
fun DefaultConverters.ShortConverter(
    returnZeroIfCantConvert: Boolean = false,
): ValueConverter<Short> =
    ValueConverter {
        it.toShortOrNull() ?: if (returnZeroIfCantConvert) 0 else throw ValueConversionException(it, Short::class)
    }

/**
 * Creates String-to-Int converter
 * @param returnZeroIfCantConvert if true, returns 0 if conversion is unsuccessful, if false throws a ValueConversionException
 * @return ValueConverter<Int>
 */
fun DefaultConverters.IntConverter(
    returnZeroIfCantConvert: Boolean = false,
): ValueConverter<Int> =
    ValueConverter {
        it.toIntOrNull() ?: if (returnZeroIfCantConvert) 0 else throw ValueConversionException(it, Int::class)
    }

/**
 * Creates String-to-Long converter
 * @param returnZeroIfCantConvert if true, returns 0 if conversion is unsuccessful, if false throws a ValueConversionException
 * @return ValueConverter<Long>
 */
fun DefaultConverters.LongConverter(
    returnZeroIfCantConvert: Boolean = false,
): ValueConverter<Long> =
    ValueConverter {
        it.toLongOrNull() ?: if (returnZeroIfCantConvert) 0 else throw ValueConversionException(it, Long::class)
    }

/**
 * Creates String-to-Double converter
 * @param returnZeroIfCantConvert if true, returns 0 if conversion is unsuccessful, if false throws a ValueConversionException
 * @return ValueConverter<Double>
 */
fun DefaultConverters.DoubleConverter(
    returnZeroIfCantConvert: Boolean = false,
): ValueConverter<Double> =
    ValueConverter {
        it.toDoubleOrNull() ?: if (returnZeroIfCantConvert) 0.0 else throw ValueConversionException(it, Double::class)
    }

/**
 * Creates String-to-Float converter
 * @param returnZeroIfCantConvert if true, returns 0 if conversion is unsuccessful, if false throws a ValueConversionException
 * @return ValueConverter<Float>
 */
fun DefaultConverters.FloatConverter(
    returnZeroIfCantConvert: Boolean = false,
): ValueConverter<Float> =
    ValueConverter {
        it.toFloatOrNull() ?: if (returnZeroIfCantConvert) 0f else throw ValueConversionException(it, Float::class)
    }
