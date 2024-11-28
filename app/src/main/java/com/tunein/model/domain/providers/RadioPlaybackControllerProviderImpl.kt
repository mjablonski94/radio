package com.tunein.model.domain.providers

import android.content.Context
import com.tunein.model.domain.RadioPlaybackController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RadioPlaybackControllerProviderImpl(
    private val context: Context,
    private val cls: Class<*>,
    private val executionDispatcher: CoroutineDispatcher = Dispatchers.Main
) : RadioPlaybackControllerProvider {

    override suspend fun provide() =
        RadioPlaybackController.SuspendBuilder().acquire(context, cls, executionDispatcher)
}