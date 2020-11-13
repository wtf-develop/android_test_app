package ru.wtfdev.kitty._dagger

import dagger.Component
import ru.wtfdev.kitty.detail.DetailsRepository
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.detail.DetailsViewModel
import ru.wtfdev.kitty.list.ImageListRepository
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.list.ImageListViewModel
import ru.wtfdev.kitty.utils.Network

@Component(modules = [Module::class])
interface Component {

    fun inject(obj: ImageListRepository)
    fun inject(obj: ImageListViewModel)
    fun inject(obj: ImageListView)
    fun inject(obj: DetailsRepository)
    fun inject(obj: DetailsViewModel)
    fun inject(obj: DetailsView)
    fun inject(obj: Network)

}