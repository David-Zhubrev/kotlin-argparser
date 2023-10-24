package com.appdav.argparser.argument

internal interface DefaultValueArgument<T> {

    val defaultValue: T

    fun createDefaultValueHelpString(defaultValue: T): String{
        return defaultValue.toString()
    }

    val defaultValueString: String
        get() = createDefaultValueHelpString(defaultValue)


}
