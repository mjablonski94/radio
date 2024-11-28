package com.tunein.components.cache

import com.tunein.components.time.TimeProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class Cache<T>(
    private val timeProvider: TimeProvider,
    private val validityDurationMs: Long = 10 * 60 * 1000,
    private val mutex: Mutex? = Mutex()
) {

    private var validUntil: Long = 0

    suspend fun saveToCache(data: T) = mutex
        ?.withLock { storeWithNextValidity(data) }
        ?: storeWithNextValidity(data)

    fun getFromCache(): T? = if (timeProvider.now() <= validUntil) {
        doGetFromCache()
    } else {
        null
    }

    private suspend fun storeWithNextValidity(data: T) {
        validUntil = timeProvider.now() + validityDurationMs
        doStore(data)
    }

    protected abstract fun doGetFromCache(): T?

    protected abstract suspend fun doStore(data: T)
}