package ru.wtfdev.kitty._models.repo

import android.widget.ImageView
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.utils.INetwork
import javax.inject.Inject

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
class ImageRepository private constructor() : IImageRepository {
    init {
        DaggerComponent.create().inject(this)
    }

    @Inject
    lateinit var network: INetwork
    override fun loadImageTo(
        img: ImageView,
        url: String,
        size: Int,
        success: ((url: String?) -> Unit)?,
        error: (() -> Unit)?
    ) {
        network.setImageMainThread(img, url, size, success, error)
    }

    companion object {
        private val imageInst = ImageRepository()
        fun getInstance() = imageInst

    }

}