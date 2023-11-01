package com.appdav.argparser.argument

/**
 * Interface that marks an argument that has default value
 */
internal interface DefaultValueArgument<T> {

    /**
     * Default value of `this` argument
     */
    val defaultValue: T

    /**
     * Method that creates string representation for default value. Used by help message creator.
     * By default, returns defaultValue.toString()
     * @param defaultValue default value of `this` argument
     * @return string representation of default value
     */
    fun createDefaultValueHelpString(defaultValue: T): String{
        return defaultValue.toString()
    }

    /**
     * Default value string representation provider
     */
    val defaultValueString: String
        get() = createDefaultValueHelpString(defaultValue)


}
