package com.appdav.argparser.argument.positional

import com.appdav.argparser.argument.ArgumentBaseInternal

/**
 * Positional argument is parsed just in the order it is declared, ignoring flags and options.
 * Positional argument has the position value. ArgParser will sort the registered arguments by `this` position and then it will try to assign
 * the input value to it.
 * NullablePositional is nullable, so it is not required and can have its value as null
 */
abstract class NullablePositional<T : Any> : ArgumentBaseInternal<T>() {

    /**
     * Position of `this` argument during the parsing process. It is not used as exact position, but as an order integer that is used to sort all the registered positional arguments
     * By default, it will be assigned with an auto-incremented integer which will be incremented on each instantiation of positional argument
     */
    open val position = nextPosition()

    protected companion object {
        private var currentPosition = 0
        fun nextPosition(): Int = currentPosition++
    }

}
