package com.appdav.argparser.converter

import com.appdav.argparser.exceptions.ValueConversionException

/**
 * Creates ValueConverter for enumeration type
 * @param values array of enum values that can be acquired by calling Enum.values()
 * @param ignoreCase if true, the comparing of enum and input string ignores case. True by default
 */
inline fun <reified T : Enum<T>> DefaultConverters.EnumConverter(
    values: Array<T>,
    ignoreCase: Boolean = true,
) = ValueConverter { input ->
    val comparator: (String, String) -> Boolean = { s1, s2 ->
        if (ignoreCase) s1.trim().lowercase() == s2.trim().lowercase()
        else s1.trim() == s2.trim()
    }
    values.find { comparator(it.name, input) }
        ?: throw ValueConversionException(
            input,
            T::class,
            "Cannot match input $input with any enum class ${T::class.qualifiedName} entry"
        )
}
