package com.tunein.components.cache

import kotlinx.coroutines.supervisorScope

abstract class CacheStrategy<T>(protected val cache: Cache<T>) {

    abstract suspend fun execute(sourceCallback: SourceCallback<T>): T?

    protected suspend fun storeToCacheAsync(data: T?) = supervisorScope {
        data?.let { cache.saveToCache(it) }
    }

    interface SourceCallback<T> {
        suspend fun getFromSource(): T?
    }
}

class CacheFirstStrategy<T>(cache: Cache<T>) : CacheStrategy<T>(cache) {

    override suspend fun execute(sourceCallback: SourceCallback<T>): T? =
        cache.getFromCache() ?: sourceCallback
            .getFromSource()
            ?.also { storeToCacheAsync(it) }
}

class SourceOnlyStrategy<T>(cache: Cache<T>) : CacheStrategy<T>(cache) {

    override suspend fun execute(sourceCallback: SourceCallback<T>): T? =
        sourceCallback.getFromSource()

}

class StaleWhileRevalidateStrategy<T>(cache: Cache<T>) : CacheStrategy<T>(cache) {

    override suspend fun execute(sourceCallback: SourceCallback<T>): T? {
        val fromCache = cache.getFromCache()

        return if (fromCache != null) {
            reFetchCacheWithSourceAsync(sourceCallback)

            fromCache
        } else {
            val fromSource = sourceCallback.getFromSource()
            storeToCacheAsync(fromSource)

            fromSource
        }
    }

    private suspend fun reFetchCacheWithSourceAsync(sourceCallback: SourceCallback<T>) = supervisorScope {
        val data = sourceCallback.getFromSource()
        storeToCacheAsync(data)
    }
}