package com.appdav.argparser.converter

fun interface ValueConverter<T> {

    fun convert(input: String): T

}
