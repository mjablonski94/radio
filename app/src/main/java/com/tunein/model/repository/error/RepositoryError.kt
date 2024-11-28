package com.tunein.model.repository.error

sealed class RepositoryError: Throwable() {
    class ServiceError : RepositoryError()
    class UnknownError: RepositoryError()
}