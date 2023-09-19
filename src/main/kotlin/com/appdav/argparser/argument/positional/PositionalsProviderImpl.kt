package com.appdav.argparser.argument.positional

import com.appdav.argparser.ArgRegistry
import com.appdav.argparser.ValueConverter
import com.appdav.argparser.argument.Validator

internal object PositionalsProviderImpl : PositionalsProvider {

    context(ArgRegistry)
    override fun <T : Any> nullable(
        name: String,
        converter: ValueConverter<T>,
        position: Int,
        description: String,
        validator: Validator<T>,
    ): NullablePositional<T> {
        val arg = object : NullablePositional<T>() {
            override val name: String = name
            override val converter: ValueConverter<T> = converter
            override val position: Int = if (position == -1) nextPosition() else if (position >= 0) position
            else throw IllegalArgumentException("Cannot assign negative position to positional argument:\nArgument: $name, position: $position")
            override val description: String = description
            override val validator: Validator<T> = validator
        }
        registerArgument(arg)
        return arg
    }

    context(ArgRegistry)
    override fun <T : Any> required(
        name: String,
        converter: ValueConverter<T>,
        position: Int,
        description: String,
        validator: Validator<T>,
    ): RequiredPositional<T> {
        val arg = object : RequiredPositional<T>() {
            override val name: String = name
            override val converter: ValueConverter<T> = converter
            override val position: Int =
                if (position == -1) nextPosition()
                else if (position >= 0) position
                else throw IllegalArgumentException("Cannot assign negative position to positional argument:\nArgument: $name, position: $position")
            override val description: String = description
            override val validator: Validator<T> = validator
        }
        registerArgument(arg)
        return arg
    }

    context(ArgRegistry)
    override fun <T : Any> withDefaultValue(
        name: String,
        converter: ValueConverter<T>,
        defaultValue: T,
        position: Int,
        description: String,
        validator: Validator<T>,
    ): PositionalWithDefaultValue<T> {
        val arg = object : PositionalWithDefaultValue<T>() {
            override val name: String = name
            override val converter: ValueConverter<T> = converter
            override val defaultValue: T = defaultValue
            override val position: Int =
                if (position == -1) nextPosition()
                else if (position >= 0) position
                else throw IllegalArgumentException("Cannot assign negative position to positional argument:\nArgument: $name, position: $position")
            override val description: String = description
            override val validator: Validator<T> = validator
        }
        registerArgument(arg)
        return arg
    }

}
