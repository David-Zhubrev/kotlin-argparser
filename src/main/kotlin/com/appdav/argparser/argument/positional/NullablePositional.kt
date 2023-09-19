package com.appdav.argparser.argument.positional

import com.appdav.argparser.argument.ArgumentBaseInternal

abstract class NullablePositional<T : Any> : ArgumentBaseInternal<T>() {

    open val position = nextPosition()

    protected companion object {
        private var currentPosition = 0
        fun nextPosition(): Int = currentPosition++
    }

}
