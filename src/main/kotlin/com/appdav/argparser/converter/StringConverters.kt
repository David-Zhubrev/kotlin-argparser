package com.appdav.argparser.converter

/**
 * Creates and returns ValueConverter that converts string input into string value
 * @param trimType type of trimming applied to input string. TrimType.BOTH by default
 * @param mutator mutation function applied to trimmed string
 * @return ValueConverter<String>
 * @see ValueConverter
 * @see TrimType
 * @see TrimType.BOTH
 */
fun DefaultConverters.StringConverter(
    trimType: TrimType = TrimType.BOTH,
    mutator: (String) -> String = { it },
): ValueConverter<String> = ValueConverter { input ->
    input.let(trimType.trimmer).let(mutator)
}

/**
 * Trim type used by DefaultConverters.stringConverter
 */
enum class TrimType(val trimmer: (String) -> String) {
    /**
     * No trimming
     */
    NONE({ it }),

    /**
     * Trims whitespaces at the start of the string
     */
    START(String::trimStart),

    /**
     * Trims whitespaces at the end of the string
     */
    END(String::trimEnd),

    /**
     * Trims whitespaces at the start and the end of the string
     */
    BOTH(String::trim)
}
