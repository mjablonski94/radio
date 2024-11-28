package com.tunein.model.service

import com.squareup.moshi.JsonAdapter
import com.tunein.components.error.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher

internal class StationsServiceImpl(
    private val jsonAdapter: JsonAdapter<StationsResponse>,
    errorHandler: ErrorHandler,
    dispatcher: CoroutineDispatcher,
) : StationsService(errorHandler, dispatcher) {

    override suspend fun getStationsFromApi(): List<StationApiData> =
        jsonAdapter.fromJson(MOCK_RESPONSE_JSON)?.data.orEmpty()
}