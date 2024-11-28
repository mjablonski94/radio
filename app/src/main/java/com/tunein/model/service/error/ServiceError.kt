package com.tunein.model.service.error

sealed class ServiceError(message: String?) : Throwable(message) {
    sealed class NetworkError(message: String?) : ServiceError(message) {
        class NoData(message: String? = null) : NetworkError(message)
        class Unauthorized(message: String? = null) : NetworkError(message)
        class Other(val code: Int, message: String?) : NetworkError(message)
    }

    class ParsingError(message: String?) : ServiceError(message)
    class EncodingError(message: String?) : ServiceError(message)
    class UnknownError(message: String?) : ServiceError(message)
}

