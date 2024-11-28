package com.tunein.ui.playback

import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    private fun getPlayer() = player
        ?: ExoPlayer.Builder(this).build()
            .also { player = it }

    private fun getMediaSession() = mediaSession
        ?: MediaSession.Builder(this, getPlayer())
            .setId(System.currentTimeMillis().toString())
            .build()
            .also { mediaSession = it }

    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession
            ?.player
            ?.let { player ->
                if (
                    !player.playWhenReady ||
                    player.mediaItemCount == 0 ||
                    player.playbackState == Player.STATE_ENDED
                ) {
                    stopSelf()
                }
            }
    }

    override fun onDestroy() {
        mediaSession?.player?.release()
        mediaSession?.release()
        mediaSession = null
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
        mediaSession ?: getMediaSession()
}
