package com.appdav.argparser

fun interface ValueConverter<T> {

    fun convert(input: String): T

}
