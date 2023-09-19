package com.appdav.argparser.converter

fun DefaultConverters.stringConverter(
    trimType: TrimType = TrimType.BOTH,
    mutator: (String) -> String = { it },
): ValueConverter<String> = ValueConverter { input ->
    input.let(trimType.trimmer).let(mutator)
}


enum class TrimType(val trimmer: (String) -> String) {
    NONE({ it }),
    START(String::trimStart),
    END(String::trimEnd),
    BOTH(String::trim)
}
