package com.tunein.model.domain.mappers

import com.tunein.model.domain.RadioStationDomainModel
import com.tunein.ui.radiolist.RadioStationUiModel

class RadioStationDomainToUiMapper {

    fun map(radioStationDomainModel: RadioStationDomainModel) = RadioStationUiModel(
        id = radioStationDomainModel.id,
        name = radioStationDomainModel.name,
        description = radioStationDomainModel.description,
        imageUrl = radioStationDomainModel.imageUrl,
        streamUrl = radioStationDomainModel.streamUrl,
        reliability = radioStationDomainModel.reliability,
        popularity = radioStationDomainModel.popularity,
        tags = radioStationDomainModel.tags
    )

    fun map(radioStationUiModel: RadioStationUiModel) = RadioStationDomainModel(
        id = radioStationUiModel.id,
        name = radioStationUiModel.name,
        description = radioStationUiModel.description,
        imageUrl = radioStationUiModel.imageUrl,
        streamUrl = radioStationUiModel.streamUrl,
        reliability = radioStationUiModel.reliability,
        popularity = radioStationUiModel.popularity,
        tags = radioStationUiModel.tags
    )
}