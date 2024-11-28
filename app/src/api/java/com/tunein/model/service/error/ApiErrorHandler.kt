package com.tunein.model.service.error

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.tunein.components.error.ErrorHandler
import retrofit2.HttpException

class ApiErrorHandler : ErrorHandler() {
    override fun map(throwable: Throwable): Throwable = when (throwable) {
        is HttpException -> when (throwable.code()) {
            404 -> ServiceError.NetworkError.NoData()
            401 -> ServiceError.NetworkError.Unauthorized()
            else -> ServiceError.NetworkError.Other(throwable.code(), throwable.message())
        }

        is JsonDataException -> ServiceError.ParsingError(throwable.message)
        is JsonEncodingException -> ServiceError.EncodingError(throwable.message)
        else -> ServiceError.UnknownError(throwable.message)
    }
}
