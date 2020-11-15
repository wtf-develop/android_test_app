package ru.wtfdev.kitty.add_link

import android.widget.ImageView
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.detail.IDetailsRepository
import ru.wtfdev.kitty.utils.AutoDisposable
import javax.inject.Inject


interface IAddLinkViewModel {
    fun save(url: String)
    fun loadImageTo(img: ImageView, url: String)
}


class AddLinkViewModel(val navigation: INavigation) : IAddLinkViewModel {
    init {
        DaggerComponent.create().inject(this)
    }


    @Inject
    lateinit var repository: IAddLinkRepository

    @Inject
    lateinit var autoDisposable: AutoDisposable

    @Inject
    lateinit var imageRepo: IImageRepository

    override fun save(url: String) {

    }

    override fun loadImageTo(img: ImageView, url: String) {

    }


    companion object {
        fun getInstance(navi: INavigation): IAddLinkViewModel =
            AddLinkViewModel(navi)
    }
}