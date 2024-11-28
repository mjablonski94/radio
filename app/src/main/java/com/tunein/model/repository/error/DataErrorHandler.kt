package com.tunein.model.repository.error

import com.tunein.components.error.ErrorHandler
import com.tunein.model.service.error.ServiceError

class DataErrorHandler : ErrorHandler() {

    override fun map(throwable: Throwable): Throwable = when (throwable) {
        is ServiceError -> RepositoryError.ServiceError()
        else -> RepositoryError.UnknownError()
    }
}
