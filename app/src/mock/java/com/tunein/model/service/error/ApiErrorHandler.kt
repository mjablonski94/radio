package com.tunein.model.service.error

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.tunein.components.error.ErrorHandler

class ApiErrorHandler : ErrorHandler() {
    override fun map(throwable: Throwable): Throwable = when(throwable) {
        is JsonDataException -> ServiceError.ParsingError(throwable.message)
        is JsonEncodingException -> ServiceError.EncodingError(throwable.message)
        else -> ServiceError.UnknownError(throwable.message)
    }
}
