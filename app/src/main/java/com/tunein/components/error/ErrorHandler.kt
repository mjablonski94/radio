package com.tunein.components.error

abstract class ErrorHandler {
    suspend fun <T> withExceptionMapped(block: suspend () -> T) = runCatching {
        block()
    }.getOrElse { throwable ->
        throw map(throwable)
    }

    abstract fun map(throwable: Throwable): Throwable
}