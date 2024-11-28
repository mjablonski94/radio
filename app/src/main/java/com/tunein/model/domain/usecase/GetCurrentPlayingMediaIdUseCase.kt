package com.tunein.model.domain.usecase

import com.tunein.components.usecase.UseCase
import com.tunein.model.domain.providers.RadioPlaybackControllerProvider

class GetCurrentPlayingMediaIdUseCase(
    private val radioPlaybackControllerProvider: RadioPlaybackControllerProvider
) : UseCase<String, Unit>() {

    override suspend fun action(params: Unit?, ): String? =
        radioPlaybackControllerProvider.provide().getCurrentMediaId()
}