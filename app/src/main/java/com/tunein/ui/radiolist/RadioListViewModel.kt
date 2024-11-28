package com.tunein.ui.radiolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.tunein.model.domain.usecase.FindNextStationUseCase
import com.tunein.model.domain.usecase.GetCurrentPlayingMediaIdUseCase
import com.tunein.model.domain.usecase.GetRadioStationsUseCase
import com.tunein.model.domain.usecase.GetRadioStationsUseCase.StationsFilter
import com.tunein.model.domain.usecase.PlaybackControlUseCase
import com.tunein.ui.playback.PlayerState
import com.tunein.model.domain.mappers.RadioStationDomainToUiMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RadioListViewModel(
    private val getRadioStationsUseCase: GetRadioStationsUseCase,
    private val getCurrentPlayingMediaIdUseCase: GetCurrentPlayingMediaIdUseCase,
    private val playbackControlUseCase: PlaybackControlUseCase,
    private val findNextStationUseCase: FindNextStationUseCase,
    private val radioStationDomainToUiMapper: RadioStationDomainToUiMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _listState = MutableLiveData<RadioListState>(
        RadioListState.Refreshing(emptyList(), StationsFilter.FetchAll, null)
    )

    val state = _listState.distinctUntilChanged()

    val nowPlaying = _listState
        .map { state -> state.lastRadioStation }
        .distinctUntilChanged()

    private val playerListener = object : Player.Listener {

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            _playerState.postValue(PlayerState.ERROR)
            clearCurrentPlayingItem()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED) {
                _playerState.postValue(PlayerState.LOADING)
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            val newPlayerState = if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
            _playerState.postValue(newPlayerState)
        }
    }

    private val _playerState = MutableLiveData(PlayerState.PAUSED)
    val playerState = _playerState.distinctUntilChanged()

    init {
        refreshStations()
        attachPlayerListener()
    }

    private fun attachPlayerListener() {
        playbackControlUseCase(
            viewModelScope,
            PlaybackControlUseCase.ControlCommand.AddListener(playerListener),
            dispatcher
        )
    }

    fun refreshStations(selectedFilter: StationsFilter = state.value?.selectedFilter ?: StationsFilter.FetchAll) {
        val progressState = RadioListState.Refreshing(state.value!!)
        _listState.postValue(progressState)

        getRadioStationsUseCase(viewModelScope, selectedFilter, dispatcher) {
            onSuccess { result ->
                val list = result?.map(radioStationDomainToUiMapper::map)
                updateStationWithCurrentPlayingRadio(list, selectedFilter)
            }
            onFailure { error ->
                val newState = RadioListState.Error(state.value!!, error.toString())
                _listState.postValue(newState)
            }
        }
    }

    private suspend fun updateStationWithCurrentPlayingRadio(
        radioList: List<RadioStationUiModel>?,
        selectedFilter: StationsFilter
    ) {
        val nowPlaying = getNowPlying(radioList)?.let { RadioStationState(it) }

        val newState = RadioListState.Idle(
            radioList.orEmpty(),
            selectedFilter,
            nowPlaying
        )

        _listState.postValue(newState)
    }

    private suspend fun getNowPlying(radioList: List<RadioStationUiModel>?): RadioStationUiModel? {
        val currentMediaId = getCurrentPlayingMediaIdUseCase.action(Unit)

        return radioList
            ?.firstOrNull { it.id == currentMediaId }
    }

    private fun schedulePlayback(radioStation: RadioStationUiModel) {
        playbackControlUseCase(
            viewModelScope,
            PlaybackControlUseCase.ControlCommand.Play(radioStationDomainToUiMapper.map(radioStation)),
            dispatcher
        )
    }

    fun selectRadioStation(radioStationDomainModel: RadioStationUiModel) {
        state.value?.let { state ->
            val newState = state
                .copy(selectedRadioStation = RadioStationState(radioStationDomainModel))

            _listState.postValue(newState)

            viewModelScope.launch(dispatcher) {
                schedulePlayback(radioStationDomainModel)
            }
        }
    }

    fun releasePlayer() {
        playbackControlUseCase(
            viewModelScope,
            PlaybackControlUseCase.ControlCommand.Release,
            dispatcher
        )
    }

    private fun clearCurrentPlayingItem() {
        state.value?.let { state ->
            val current = state.lastRadioStation ?: return@clearCurrentPlayingItem
            val newState = state.copy(selectedRadioStation = current.copy(hasError = true))
            _listState.postValue(newState)
        }
    }

    fun pausePlay() {
        if (_playerState.value == PlayerState.PLAYING) {
            playbackControlUseCase(
                viewModelScope,
                PlaybackControlUseCase.ControlCommand.Pause,
                dispatcher
            )
        } else {
            playbackControlUseCase(
                viewModelScope,
                PlaybackControlUseCase.ControlCommand.Resume,
                dispatcher
            )
        }
    }

    fun playNext() {
        findNextStationUseCase(
            viewModelScope,
            FindNextStationUseCase.Params.Next(
                list = state.value?.radioStations
                    ?.map(radioStationDomainToUiMapper::map),
                current = state.value
                    ?.lastRadioStation?.selectedRadioStation
                    ?.let(radioStationDomainToUiMapper::map)
            ),
            dispatcher
        ) {
            onSuccess { result ->
                result
                    ?.let(radioStationDomainToUiMapper::map)
                    ?.let(::selectRadioStation)
            }
        }
    }

    fun playPrevious() {
        findNextStationUseCase(
            viewModelScope,
            FindNextStationUseCase.Params.Previous(
                list = state.value?.radioStations?.map(radioStationDomainToUiMapper::map),
                current = state.value?.lastRadioStation?.selectedRadioStation
                    ?.let(radioStationDomainToUiMapper::map)
            ),
            dispatcher
        ) {
            onSuccess { result ->
                result
                    ?.let(radioStationDomainToUiMapper::map)
                    ?.let(::selectRadioStation)
            }
        }
    }
}
