package com.tunein.model.repository

import com.tunein.model.service.StationApiData

class ServiceToRepositoryDataMapper {
    fun map(stationApiData: StationApiData): StationDataModel? = with(stationApiData) {
        if (id.isBlank() || name.isBlank() || streamUrl.isBlank()) null
        else StationDataModel(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            streamUrl = streamUrl,
            reliability = reliability,
            popularity = popularity,
            tags = tags.orEmpty()
        )
    }
}
