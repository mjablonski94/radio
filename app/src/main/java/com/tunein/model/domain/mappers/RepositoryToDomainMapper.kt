package com.tunein.model.domain.mappers

import com.tunein.model.domain.RadioStationDomainModel
import com.tunein.model.repository.StationDataModel

class RepositoryToDomainMapper {

    fun map(stationDataModel: StationDataModel): RadioStationDomainModel = with(stationDataModel) {
        RadioStationDomainModel(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            streamUrl = streamUrl,
            reliability = reliability,
            popularity = popularity,
            tags = tags
        )
    }
}