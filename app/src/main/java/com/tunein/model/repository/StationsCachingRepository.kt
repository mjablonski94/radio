package com.tunein.model.repository

import com.tunein.components.cache.CacheStrategy
import com.tunein.components.error.ErrorHandler
import com.tunein.model.service.StationsService

class StationsCachingRepository(
    private val stationsService: StationsService,
    private val cacheStrategy: CacheStrategy<List<StationDataModel>>,
    private val serviceToRepositoryDataMapper: ServiceToRepositoryDataMapper,
    private val errorHandler: ErrorHandler
) : StationsRepository, CacheStrategy.SourceCallback<List<StationDataModel>> {

    override suspend fun getStations(): List<StationDataModel> =
        cacheStrategy
            .execute(this)
            .orEmpty()

    override suspend fun getFromSource(): List<StationDataModel> =
        errorHandler.withExceptionMapped {
            stationsService
                .getStations()
                .mapNotNull(serviceToRepositoryDataMapper::map)
        }
}
