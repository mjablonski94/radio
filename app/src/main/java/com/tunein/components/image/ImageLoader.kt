package com.tunein.components.image

import android.net.Uri
import android.util.Size
import android.widget.ImageView
import androidx.annotation.DrawableRes

import com.tunein.R

interface ImageLoader {

    fun loadImage(
        into: ImageView,
        uri: Uri,
        options: Options = Options()
    )

    fun loadImage(
        into: ImageView,
        url: String,
        options: Options = Options(),
    )

    fun loadImage(
        into: ImageView,
        uri: Uri,
        signature: String?,
        options: Options = Options()
    )

    class Options @JvmOverloads constructor(
        val size: Size? = null,
        val animateCrossFade: Boolean = true,
        val cacheOptions: CacheOptions = CacheOptions.AUTO,
        val centerCrop: Boolean = false,
        val fitCenter: Boolean = false,
        @DrawableRes val errorPlaceholder: Int? = R.drawable.ic_no_image
    )


    enum class CacheOptions {
        AUTO, DISABLED, ENABLED
    }
}