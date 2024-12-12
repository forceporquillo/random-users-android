package dev.forcecodes.albertsons.randomuser.presentation.view

import android.content.Context
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade

internal inline val Context.imageLoader: ImageLoader
    get() = ImageLoaderSingleton.get(this)

internal object ImageLoaderSingleton {

    private var imageLoader: ImageLoader? = null

    fun get(context: Context): ImageLoader {
        if (imageLoader == null) {
            imageLoader = ImageLoader.Builder(context)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build()
        }
        return imageLoader!!
    }
}
