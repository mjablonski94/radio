package com.tunein.model.domain.usecase

import com.tunein.components.usecase.UseCase
import com.tunein.model.domain.RadioStationDomainModel
import com.tunein.model.domain.mappers.RepositoryToDomainMapper
import com.tunein.model.repository.StationsRepository

class GetRadioStationsUseCase(
    private val stationsRepository: StationsRepository,
    private val repositoryToDomainMapper: RepositoryToDomainMapper
) : UseCase<List<RadioStationDomainModel>, GetRadioStationsUseCase.StationsFilter>() {

    override suspend fun action(params: StationsFilter?): List<RadioStationDomainModel> {
        params ?: return emptyList()
        val stations = stationsRepository
            .getStations()
            .map(repositoryToDomainMapper::map)
            .sortedBy(RadioStationDomainModel::name)

        return when (params) {
            is StationsFilter.FetchAll -> stations
            is StationsFilter.FetchByPopularity ->
                stations.filter { (it.popularity ?: 0.0) >= params.popularity }
                    .sortedBy { it.popularity }

            is StationsFilter.FetchByReliability ->
                stations.filter { (it.reliability ?: 0) >= params.reliability }
                    .sortedBy { it.reliability }

            is StationsFilter.FetchBySearch -> stations.filter {
                val query = params.query.lowercase()
                val name = it.name.lowercase()
                val description = it.description?.lowercase() ?: ""
                val tags = it.tags.joinToString(" ").lowercase()

                name.contains(query) ||
                        description.contains(query) ||
                        tags.contains(query) ||
                        query.contains(name) ||
                        query.contains(description)
            }

            is StationsFilter.FetchByTag -> stations.filter {
                it.tags.contains(params.tag)
            }
        }
    }

    sealed interface StationsFilter {
        data object FetchAll : StationsFilter
        data class FetchByTag(val tag: String) : StationsFilter
        data class FetchBySearch(val query: String) : StationsFilter
        data class FetchByReliability(val reliability: Int) : StationsFilter
        data class FetchByPopularity(val popularity: Double) : StationsFilter
    }
}