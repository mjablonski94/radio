package com.tunein.ui.radiolist

import com.tunein.model.domain.usecase.GetRadioStationsUseCase

sealed class RadioListState(
    val radioStations: List<RadioStationUiModel>,
    val selectedFilter: GetRadioStationsUseCase.StationsFilter,
    val lastRadioStation: RadioStationState?
) {
    class Error(
        state: RadioListState,
        val error: String
    ) : RadioListState(state.radioStations, state.selectedFilter, state.lastRadioStation)

    class Refreshing(
        radioStations: List<RadioStationUiModel>,
        selectedFilter: GetRadioStationsUseCase.StationsFilter,
        selectedRadioStation: RadioStationState? = null
    ) : RadioListState(radioStations, selectedFilter, selectedRadioStation) {
        constructor(state: RadioListState) : this(
            state.radioStations,
            state.selectedFilter,
            state.lastRadioStation
        )
    }

    class Idle(
        radioStations: List<RadioStationUiModel>,
        selectedFilter: GetRadioStationsUseCase.StationsFilter,
        selectedRadioStation: RadioStationState? = null
    ) : RadioListState(radioStations, selectedFilter, selectedRadioStation) {
        constructor(state: RadioListState) : this(
            state.radioStations,
            state.selectedFilter,
            state.lastRadioStation
        )
    }

    fun copy(
        radioStations: List<RadioStationUiModel> = this.radioStations,
        selectedFilter: GetRadioStationsUseCase.StationsFilter = this.selectedFilter,
        selectedRadioStation: RadioStationState? = this.lastRadioStation
    ): RadioListState = when (this) {
        is Refreshing -> Refreshing(radioStations, selectedFilter, selectedRadioStation)
        else -> Idle(radioStations, selectedFilter, selectedRadioStation)
    }
}

data class RadioStationState(
    val selectedRadioStation: RadioStationUiModel,
    val hasError: Boolean = false
)

data class RadioStationUiModel(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val streamUrl: String,
    val reliability: Int?,
    val popularity: Double?,
    val tags: List<String>,
)