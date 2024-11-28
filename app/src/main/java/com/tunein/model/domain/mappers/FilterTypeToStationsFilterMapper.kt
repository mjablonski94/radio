package com.tunein.model.domain.mappers

import com.tunein.model.domain.usecase.GetRadioStationsUseCase
import com.tunein.ui.radiolist.FilterType

class FilterTypeToStationsFilterMapper {

    fun map(filterType: FilterType, data: String? = null): GetRadioStationsUseCase.StationsFilter {
        if (data.isNullOrBlank()) return GetRadioStationsUseCase.StationsFilter.FetchAll
        return when (filterType) {
            FilterType.None -> GetRadioStationsUseCase.StationsFilter.FetchAll

            FilterType.Popularity -> data.toDoubleOrNull()
                ?.let { GetRadioStationsUseCase.StationsFilter.FetchByPopularity(it) }
                ?: GetRadioStationsUseCase.StationsFilter.FetchAll

            FilterType.Reliability -> data.toIntOrNull()
                ?.let { GetRadioStationsUseCase.StationsFilter.FetchByReliability(it) }
                ?: GetRadioStationsUseCase.StationsFilter.FetchAll

            FilterType.Search -> GetRadioStationsUseCase.StationsFilter.FetchBySearch(data)

            FilterType.Tag -> GetRadioStationsUseCase.StationsFilter.FetchByTag(data)
        }
    }

    fun map(stationsFilter: GetRadioStationsUseCase.StationsFilter): FilterType {
        return when (stationsFilter) {
            GetRadioStationsUseCase.StationsFilter.FetchAll -> FilterType.None
            is GetRadioStationsUseCase.StationsFilter.FetchByPopularity -> FilterType.Popularity
            is GetRadioStationsUseCase.StationsFilter.FetchByReliability -> FilterType.Reliability
            is GetRadioStationsUseCase.StationsFilter.FetchBySearch -> FilterType.Search
            is GetRadioStationsUseCase.StationsFilter.FetchByTag -> FilterType.Tag
        }
    }
}