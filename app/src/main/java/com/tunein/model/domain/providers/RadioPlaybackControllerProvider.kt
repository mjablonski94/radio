package com.tunein.model.domain.providers

import com.tunein.model.domain.RadioPlaybackController

interface RadioPlaybackControllerProvider {

    suspend fun provide(): RadioPlaybackController
}