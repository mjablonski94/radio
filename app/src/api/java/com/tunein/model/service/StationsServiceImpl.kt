package com.tunein.model.service

import com.tunein.model.service.error.ApiErrorHandler
import kotlinx.coroutines.CoroutineDispatcher

internal class StationsServiceImpl(
    private val stationsApiService: StationsApiService,
    apiErrorHandler: ApiErrorHandler,
    dispatcher: CoroutineDispatcher,
) : StationsService(apiErrorHandler, dispatcher) {

    override suspend fun getStationsFromApi(): List<StationApiData> = stationsApiService.getStations().data
}