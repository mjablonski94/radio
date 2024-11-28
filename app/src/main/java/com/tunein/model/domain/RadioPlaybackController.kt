package com.tunein.model.domain

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PercentageRating
import androidx.media3.common.Player
import androidx.media3.common.StarRating
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.tunein.components.ext.applyWhen
import com.tunein.components.ext.suspendGet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RadioPlaybackController private constructor(
    private val mediaController: MediaController,
    private val executionDispatcher: CoroutineDispatcher = Dispatchers.Main,
) {

    private val listeners = mutableListOf<Player.Listener>()

    suspend fun play(radioStation: RadioStationDomainModel) {
        with(mediaController) {
            if (getCurrentMediaId() == radioStation.id) {
                // do nothing, this source is currently playing
                return
            }

            withContext(executionDispatcher) {
                if (hasPlayerOpenedTheStream()) {
                    stop()
                }

                clearMediaItems()

                val metadata: MediaMetadata = radioStation.toMediaType()
                val mediaItem = radioStation.toMediaItem(metadata)

                setMediaItem(mediaItem)
                prepare()

                playWhenReady = true
            }
        }
    }

    suspend fun addListener(listener: Player.Listener) {
        withContext(executionDispatcher) {
            listeners.add(listener)
            mediaController.addListener(listener)
        }
    }

    suspend fun removeListener(listener: Player.Listener) {
        withContext(executionDispatcher) {
            listeners.remove(listener)
            mediaController.removeListener(listener)
        }
    }

    private fun RadioStationDomainModel.toMediaType(): MediaMetadata {
        val artworkUri = runCatching { Uri.parse(imageUrl) }.getOrNull()

        return MediaMetadata.Builder()
            .applyWhen(artworkUri != null) { setArtworkUri(artworkUri) }
            .setTitle(name)
            .applyWhen(!description.isNullOrBlank()) { setDescription(description) }
            .applyWhen(popularity != null) {
                setOverallRating(StarRating(5, popularity!!.toFloat()))
            }
            .applyWhen(reliability != null) {
                setUserRating(PercentageRating(reliability!!.toFloat()))
            }
            .setMediaType(MediaMetadata.MEDIA_TYPE_RADIO_STATION)
            .build()
    }

    private fun RadioStationDomainModel.toMediaItem(metadata: MediaMetadata) =
        MediaItem.Builder()
            .setUri(streamUrl)
            .setMediaId(id)
            .setMediaMetadata(metadata)
            .build()

    private suspend fun MediaController.hasPlayerOpenedTheStream() =
        withContext(executionDispatcher) {
            playbackState == Player.STATE_READY || playbackState == Player.STATE_BUFFERING
        }

    suspend fun release() {
        withContext(executionDispatcher) {
            listeners.forEach(mediaController::removeListener)
            mediaController.release()
        }
    }

    suspend fun getCurrentMediaId() = withContext(executionDispatcher) {
        mediaController.currentMediaItem?.mediaId
    }

    suspend fun pause() {
        withContext(executionDispatcher) {
            mediaController.pause()
        }
    }

    suspend fun resume() {
        withContext(executionDispatcher) {
            mediaController.play()
        }
    }

    suspend fun stop() {
        withContext(executionDispatcher) {
            if (mediaController.hasPlayerOpenedTheStream()) {
                stop()
            }
        }
    }

    class SuspendBuilder {
        suspend fun acquire(
            context: Context,
            cls: Class<*>,
            executionDispatcher: CoroutineDispatcher = Dispatchers.Main
        ): RadioPlaybackController {
            val sessionToken = SessionToken(context, ComponentName(context, cls))

            val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
            val mediaController = controllerFuture.suspendGet(dispatcher = executionDispatcher)
            return RadioPlaybackController(mediaController, executionDispatcher)
        }
    }
}