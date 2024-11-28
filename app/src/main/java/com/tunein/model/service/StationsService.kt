package com.tunein.model.service

import com.tunein.components.error.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class StationsService(
    private val errorHandler: ErrorHandler,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getStations() = withContext(dispatcher) {
        errorHandler.withExceptionMapped { getStationsFromApi() }
    }

    protected abstract suspend fun getStationsFromApi(): List<StationApiData>
}