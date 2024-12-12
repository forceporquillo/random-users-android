package dev.forcecodes.albertsons.randomuser.presentation.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil3.load

@BindingAdapter("loadImage")
fun ImageView.loadImage(url: String) {
    load(url, context.imageLoader)
}
