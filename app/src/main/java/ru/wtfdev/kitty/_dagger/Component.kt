package ru.wtfdev.kitty._dagger

import dagger.Component
import ru.wtfdev.kitty.DialogActivity
import ru.wtfdev.kitty.MainActivity
import ru.wtfdev.kitty._models.repo.ImageRepository
import ru.wtfdev.kitty.add_link.AddLinkRepository
import ru.wtfdev.kitty.add_link.AddLinkView
import ru.wtfdev.kitty.add_link.AddLinkViewModel
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


    fun inject(obj: AddLinkRepository)
    fun inject(obj: AddLinkViewModel)
    fun inject(obj: AddLinkView)

    fun inject(obj: MainActivity)
    fun inject(obj: DialogActivity)
    fun inject(obj: ImageRepository)


}