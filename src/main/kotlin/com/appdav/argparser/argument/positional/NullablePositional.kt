package com.appdav.argparser.argument.positional

import com.appdav.argparser.argument.ArgumentBase

abstract class NullablePositional<T : Any> : ArgumentBase<T>() {

    open val position = nextPosition()

    protected companion object {
        private var currentPosition = 0
        fun nextPosition(): Int = currentPosition++
    }

}
