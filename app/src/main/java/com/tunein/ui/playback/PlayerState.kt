package com.tunein.ui.playback

import com.tunein.R

enum class PlayerState(val buttonTextRes: Int) {
    PLAYING(R.string.pause),
    PAUSED(R.string.play),
    ERROR(R.string.error),
    LOADING(R.string.loading)
}