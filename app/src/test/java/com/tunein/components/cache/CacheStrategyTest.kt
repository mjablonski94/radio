package com.tunein.components.cache

import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CacheStrategyTest {

    @Test
    fun test_CacheFirstStrategy() = runBlocking {
        //given
        val cache: Cache<String> = mockk(relaxed = true) {
            every { getFromCache() } returns "cache"
        }
        val strategy = CacheFirstStrategy(cache)
        val sourceCallback = createFakeSourceCallback("source")

        //when
        val actual = strategy.execute(sourceCallback)

        //then
        assertEquals(actual, "cache")
    }

    @Test
    fun test_SourceOnlyStrategy() = runBlocking {
        //given
        val cache: Cache<String> = mockk(relaxed = true) {
            every { getFromCache() } returns "cache"
        }
        val strategy = SourceOnlyStrategy(cache)
        val sourceCallback = createFakeSourceCallback("source")

        //when
        val actual = strategy.execute(sourceCallback)

        //then
        assertEquals(actual, "source")
    }

    @Test
    fun test_StaleWhileRevalidateStrategy_with_cached_data() = runBlocking {
        //given
        val cache: Cache<String> = mockk(relaxed = true) {
            every { getFromCache() } returns "cache"
        }
        val strategy = spyk(StaleWhileRevalidateStrategy(cache))
        val sourceCallback = createFakeSourceCallback("source")

        //when
        val actual = strategy.execute(sourceCallback)

        //then
        assertEquals(actual, "cache")
        coVerifyOrder {
            cache.getFromCache()
            sourceCallback.getFromSource()
            cache.saveToCache(any())
        }
    }

    @Test
    fun test_StaleWhileRevalidateStrategy_with_out_cached_data() = runBlocking {
        //given
        val cache: Cache<String> = mockk(relaxed = true) {
            every { getFromCache() } returns null
        }
        val strategy = spyk(StaleWhileRevalidateStrategy(cache))
        val sourceCallback = createFakeSourceCallback("source")

        //when
        val actual = strategy.execute(sourceCallback)

        //then
        assertEquals(actual, "source")
        coVerifyOrder {
            cache.getFromCache()
            sourceCallback.getFromSource()
            cache.saveToCache(any())
        }
    }


    private fun createFakeSourceCallback(data: String?) =
        object : CacheStrategy.SourceCallback<String> {
            override suspend fun getFromSource(): String? {
                return data
            }
        }
}


