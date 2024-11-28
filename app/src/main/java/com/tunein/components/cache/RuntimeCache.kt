package com.tunein.components.cache

import com.tunein.components.time.TimeProvider

class RuntimeCache<T>(
     timeProvider: TimeProvider,
) : Cache<T>(timeProvider) {

    private var data: T? = null

    override suspend fun doStore(data: T) {
        this.data = data
    }

    override fun doGetFromCache(): T? = data
}
