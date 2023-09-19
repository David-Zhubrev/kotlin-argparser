package com.appdav.argparser.converter

val DefaultConverters.FlagConverter: ValueConverter<Boolean>
    get() = ValueConverter {
        if (it.isBlank()) true
        else when (it) {
            "y", "yes", "true" -> true
            "n", "no", "false" -> false
            else -> throw IllegalArgumentException("Unknown boolean argument value: $it")
        }
    }

val DefaultConverters.BooleanConverter: ValueConverter<Boolean>
    get() = ValueConverter { input ->
        when (input) {
            "y", "yes", "true" -> true
            "n", "no", "false" -> false
            else -> throw IllegalArgumentException("Unknown boolean argument value: $input")
        }
    }
