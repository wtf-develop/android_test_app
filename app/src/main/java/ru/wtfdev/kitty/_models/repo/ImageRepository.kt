package ru.wtfdev.kitty._models.repo

import android.widget.ImageView
import ru.wtfdev.kitty._models.INetwork

//Interface for loading images
//Interface for loading images
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

//Implementation for loading images
//Implementation for loading images
//Implementation for loading images
class ImageRepository(val network: INetwork) : IImageRepository {

    override fun loadImageTo(
        img: ImageView,
        url: String,
        size: Int,
        success: ((url: String?) -> Unit)?,
        error: (() -> Unit)?
    ) {
        network.setImageMainThread(img, url, size, success, error)
    }
}