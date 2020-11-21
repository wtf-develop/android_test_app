package ru.wtfdev.kitty._models.repo.implementation

import android.widget.ImageView
import ru.wtfdev.kitty._models.network.INetwork
import ru.wtfdev.kitty._models.repo.IImageRepository

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