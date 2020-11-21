package ru.wtfdev.kitty._models.repo

import android.widget.ImageView

//Interface for loading images
interface IImageRepository {
    fun loadImageTo(
        img: ImageView,
        url: String,
        size: Int,
        success: ((url: String?) -> Unit)? = null,
        error: (() -> Unit)? = null
    )
}
