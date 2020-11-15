package ru.wtfdev.kitty.add_link

import android.widget.ImageView
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._navigation.INavigation


interface IAddLinkViewModel {
    fun save(url: String)
    fun loadImageTo(img: ImageView, url: String)
}


class AddLinkViewModel(val navigation: INavigation) : IAddLinkViewModel {
    init {
        DaggerComponent.create().inject(this)
    }

    override fun save(url: String) {

    }

    override fun loadImageTo(img: ImageView, url: String) {

    }


    companion object {
        fun getInstance(navi: INavigation): IAddLinkViewModel =
            AddLinkViewModel(navi)
    }
}