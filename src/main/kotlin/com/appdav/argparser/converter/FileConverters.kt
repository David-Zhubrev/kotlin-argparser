package com.appdav.argparser.converter

import com.appdav.argparser.exceptions.ValueConversionException
import java.io.File
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile


/**
 * Denotes which conditions should be checked when using default file converters
 */
enum class FileConverterCheckType {
    EXISTING_DIR,
    EXISTING_FILE,
    DIR,
    FILE
}


/**
 * Value converter that simply creates java.io.File instance from input string
 * @see java.io.File
 * @see ValueConverter
 */
val DefaultConverters.FileConverter: ValueConverter<File>
    get() = ValueConverter {
        try {
            File(it)
        } catch (e: Exception) {
            throw ValueConversionException(it, File::class, e.message)
        }
    }

/**
 * Value converter that simply creates java.nio.file.Path instance from input string
 * @see java.nio.file.Path
 * @see ValueConverter
 */
val DefaultConverters.PathConverter: ValueConverter<Path>
    get() = ValueConverter {
        try {
            Path.of(URI.create(it))
        } catch (e: Exception) {
            throw ValueConversionException(it, Path::class, e.message)
        }
    }

/**
 * Value converter that simply creates java.nio.file.Path instance from input string
 * @param fileConverterCheckType type of check that should be applied to created Path instance
 * @param returnAbsoluteFile if true, returns Path.toAbsolutePath()
 * @see FileConverterCheckType
 * @see java.nio.file.Path
 * @see java.nio.file.Path.toAbsolutePath
 * @see ValueConverter
 */
fun DefaultConverters.PathConverter(
    fileConverterCheckType: FileConverterCheckType,
    returnAbsoluteFile: Boolean = false,
) = ValueConverter {
    val result: Path
    try {
        result = Path.of(URI.create(it))
    } catch (e: Exception) {
        throw ValueConversionException(it, Path::class, e.message)
    }
    val check = when (fileConverterCheckType) {
        FileConverterCheckType.EXISTING_DIR -> result.isDirectory() && result.exists()
        FileConverterCheckType.EXISTING_FILE -> result.isRegularFile() && result.exists()
        FileConverterCheckType.DIR -> result.isDirectory()
        FileConverterCheckType.FILE -> result.isRegularFile()
    }
    if (!check) throw ValueConversionException(it, File::class, "File converter check condition is not satisfied")
    if (returnAbsoluteFile) result.toAbsolutePath() else result
}

/**
 * Value converter that simply creates java.io.Path instance from input string
 * @param fileConverterCheckType type of check that should be applied to created File instance
 * @param returnAbsoluteFile if true, returns File.absoluteFile
 * @see FileConverterCheckType
 * @see java.io.File
 * @see java.io.File.getAbsoluteFile
 * @see ValueConverter
 */
fun DefaultConverters.FileConverter(
    fileConverterCheckType: FileConverterCheckType,
    returnAbsoluteFile: Boolean = true,
) = ValueConverter {


    val result: File
    try {
        result = File(it)
    } catch (e: Exception) {
        throw ValueConversionException(it, File::class, e.message)
    }
    val check = when (fileConverterCheckType) {
        FileConverterCheckType.EXISTING_DIR -> result.isDirectory && result.exists()
        FileConverterCheckType.EXISTING_FILE -> result.isFile && result.exists()
        FileConverterCheckType.DIR -> result.isDirectory
        FileConverterCheckType.FILE -> result.isFile
    }
    if (!check) throw ValueConversionException(it, File::class, "File converter check condition is not satisfied")
    if (returnAbsoluteFile) result.absoluteFile else result
}
