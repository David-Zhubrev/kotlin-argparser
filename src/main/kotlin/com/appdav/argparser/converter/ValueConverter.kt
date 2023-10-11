package com.appdav.argparser.converter

/**
 * Functional interface that converts string input into specified type
 */
fun interface ValueConverter<T> {

    /**
     * Converts string input into specified type
     * @param input input string from the command line
     * @return instance of specified type
     */
    fun convert(input: String): T

}
