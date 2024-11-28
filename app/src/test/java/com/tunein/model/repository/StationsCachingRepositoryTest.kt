package com.tunein.model.repository

import com.tunein.components.cache.Cache
import com.tunein.components.cache.CacheFirstStrategy
import com.tunein.components.cache.CacheStrategy
import com.tunein.model.service.StationsService
import com.tunein.model.repository.error.RepositoryError
import com.tunein.model.repository.error.DataErrorHandler
import com.tunein.model.service.error.ServiceError
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class StationsCachingRepositoryTest {

    private lateinit var stationsService: StationsService
    private lateinit var cache: Cache<List<StationDataModel>>
    private lateinit var cacheStrategy: CacheStrategy<List<StationDataModel>>
    private lateinit var serviceToRepositoryDataMapper: ServiceToRepositoryDataMapper
    private lateinit var errorHandler: DataErrorHandler
    private lateinit var repository: StationsCachingRepository

    @Before
    fun setUp() {
        stationsService = mockk(relaxed = true)
        cache = mockk(relaxed = true)
        cacheStrategy = spyk(CacheFirstStrategy(cache))
        serviceToRepositoryDataMapper = mockk()
        errorHandler = DataErrorHandler()

        repository = StationsCachingRepository(
            stationsService = stationsService,
            cacheStrategy = cacheStrategy,
            serviceToRepositoryDataMapper = serviceToRepositoryDataMapper,
            errorHandler = errorHandler
        )
    }

    @Test
    fun `should return stations from cache when available`() = runBlocking {
        // Given
        val cachedData = listOf(mockk<StationDataModel>())
        coEvery { cacheStrategy.execute(any()) } returns cachedData

        // When
        val result = repository.getStations()

        // Then
        assertEquals(cachedData, result)
        coVerify { cacheStrategy.execute(any()) }
    }

    @Test
    fun `should fetch stations from source when cache is empty`() = runBlocking {
        // Given
        coEvery { cache.getFromCache() } returns null
        coEvery { stationsService.getStations() } returns listOf(mockk())
        coEvery { serviceToRepositoryDataMapper.map(any()) } returns mockk()

        // When
        val result = repository.getStations()

        // Then
        assertTrue(result.isNotEmpty())
        coVerify {
            stationsService.getStations()
            cache.saveToCache(result)
        }
    }

    @Test
    fun `should handle error when exception is thrown in getFromSource`() = runBlocking {
        // Given
        coEvery { stationsService.getStations() } throws Exception()

        // When
        val exception = runCatching {
            repository.getFromSource()
        }.exceptionOrNull()

        // Then
        assertNotNull(exception)
    }

    @Test
    fun `should map throwable to expected RepositoryError in error handler`() = runBlocking {
        // Given
        val serviceError = ServiceError.NetworkError.Unauthorized()
        val errorHandler = DataErrorHandler()

        // When
        val mappedError = errorHandler.map(serviceError)

        // Then
        assertTrue(mappedError is RepositoryError.ServiceError)
    }
}

