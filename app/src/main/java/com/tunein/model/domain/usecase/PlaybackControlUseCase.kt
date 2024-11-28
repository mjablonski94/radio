package com.tunein.model.domain.usecase

import androidx.media3.common.Player
import com.tunein.components.usecase.UseCase
import com.tunein.model.domain.providers.RadioPlaybackControllerProvider
import com.tunein.model.domain.RadioStationDomainModel

class PlaybackControlUseCase(
    private val radioPlaybackControllerProvider: RadioPlaybackControllerProvider
) : UseCase<Unit, PlaybackControlUseCase.ControlCommand>() {

    override suspend fun action(params: ControlCommand?) {
        val radioPlaybackController = radioPlaybackControllerProvider.provide()
        when (params) {
            ControlCommand.Pause -> radioPlaybackController.pause()
            is ControlCommand.Play -> radioPlaybackController.play(params.radioStationDomainModel)
            ControlCommand.Release -> radioPlaybackController.release()
            ControlCommand.Resume -> radioPlaybackController.resume()
            is ControlCommand.AddListener -> radioPlaybackController.addListener(params.listener)
            is ControlCommand.RemoveListener -> radioPlaybackController.removeListener(params.listener)
            else -> radioPlaybackController.stop()
        }
    }

    sealed interface ControlCommand {
        data class Play(val radioStationDomainModel: RadioStationDomainModel) : ControlCommand
        data object Pause : ControlCommand
        data object Resume : ControlCommand
        data object Stop : ControlCommand
        data object Release : ControlCommand
        data class AddListener(val listener: Player.Listener) : ControlCommand
        data class RemoveListener(val listener: Player.Listener) : ControlCommand
    }
}