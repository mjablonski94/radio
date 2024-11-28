package com.tunein.components.image

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.signature.ObjectKey
import com.tunein.components.ext.applyWhen

class GlideImageLoader : ImageLoader {

    override fun loadImage(
        into: ImageView,
        uri: Uri,
        options: ImageLoader.Options
    ) {
        Glide.with(into.context)
            .load(uri)
            .applyOptions(options)
            .into(into)
    }

    override fun loadImage(
        into: ImageView,
        url: String,
        options: ImageLoader.Options,
    ) {
        Glide.with(into.context)
            .load(url)
            .applyOptions(options)
            .into(into)
    }

    override fun loadImage(
        into: ImageView,
        uri: Uri,
        signature: String?,
        options: ImageLoader.Options
    ) {
        Glide.with(into.context)
            .load(uri)
            .let { builder ->
                signature?.let { builder.signature(ObjectKey(it)) } ?: builder
            }
            .applyOptions(options)
            .into(into)
    }

    private fun RequestBuilder<Drawable>.applyOptions(options: ImageLoader.Options) =
        when (options.cacheOptions) {
            ImageLoader.CacheOptions.AUTO -> diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            ImageLoader.CacheOptions.DISABLED -> diskCacheStrategy(DiskCacheStrategy.NONE)
            ImageLoader.CacheOptions.ENABLED -> diskCacheStrategy(DiskCacheStrategy.ALL)
        }
            .applyWhen(options.centerCrop) { centerCrop() }
            .applyWhen(options.fitCenter) { optionalFitCenter() }
            .applyWhen(options.errorPlaceholder != null) { error(options.errorPlaceholder) }
            .applyWhen(options.animateCrossFade) { transition(DrawableTransitionOptions.withCrossFade()) }
            .applyWhen(options.size != null) { override(options.size!!.width, options.size.height) }
}