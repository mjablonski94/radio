package com.tunein.model.domain.usecase

import com.tunein.components.usecase.UseCase
import com.tunein.model.domain.RadioStationDomainModel

class FindNextStationUseCase : UseCase<RadioStationDomainModel, FindNextStationUseCase.Params>() {

    override suspend fun action(params: Params?): RadioStationDomainModel? {
        params?.list?.isEmpty() ?: return null

        return with(params) {
            val selectedRadioStationIndex = list?.indexOf(current) ?: return null

            when (this) {
                is Params.Next -> {
                    list.getOrNull(selectedRadioStationIndex + 1)
                        ?: list.first()
                }

                is Params.Previous -> {
                    list.getOrNull(selectedRadioStationIndex - 1)
                        ?: list.last()
                }
            }
        }
    }

    sealed class Params(
        val list: List<RadioStationDomainModel>?,
        val current: RadioStationDomainModel?
    ) {
        class Next(
            list: List<RadioStationDomainModel>?,
            current: RadioStationDomainModel?
        ) : Params(list, current)

        class Previous(
            list: List<RadioStationDomainModel>?,
            current: RadioStationDomainModel?
        ) : Params(list, current)
    }
}