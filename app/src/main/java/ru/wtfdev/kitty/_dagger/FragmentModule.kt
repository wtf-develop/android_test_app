package ru.wtfdev.kitty._dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import ru.wtfdev.kitty._models.INetwork
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.add_link.AddLinkRepository
import ru.wtfdev.kitty.add_link.AddLinkViewModel
import ru.wtfdev.kitty.add_link.IAddLinkRepository
import ru.wtfdev.kitty.add_link.IAddLinkViewModel
import ru.wtfdev.kitty.detail.DetailsRepository
import ru.wtfdev.kitty.detail.DetailsViewModel
import ru.wtfdev.kitty.detail.IDetailsRepository
import ru.wtfdev.kitty.detail.IDetailsViewModel
import ru.wtfdev.kitty.list.IImageListRepository
import ru.wtfdev.kitty.list.IImageListViewModel
import ru.wtfdev.kitty.list.ImageListRepository
import ru.wtfdev.kitty.list.ImageListViewModel
import ru.wtfdev.kitty.utils.AutoDisposable


@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {


    @Provides
    @FragmentScoped
    fun getListRepo(network: INetwork): IImageListRepository = ImageListRepository(network)

    @Provides
    @FragmentScoped
    fun getDetailsRepo(): IDetailsRepository = DetailsRepository()


    @Provides
    @FragmentScoped
    fun getAddLink(network: INetwork): IAddLinkRepository = AddLinkRepository(network)


    @Provides
    @FragmentScoped
    fun getAutoDisposable(): AutoDisposable = AutoDisposable()


    @Provides
    @FragmentScoped
    fun getAddLinkViewModel(
        navigation: INavigation,
        repository: IAddLinkRepository,
        autoDisposable: AutoDisposable,
        imageRepo: IImageRepository
    ): IAddLinkViewModel = AddLinkViewModel(navigation, repository, autoDisposable, imageRepo)

    @Provides
    @FragmentScoped
    fun getDetailsViewModel(
        navigation: INavigation,
        repository: IDetailsRepository,
        autoDisposable: AutoDisposable,
        imageRepo: IImageRepository
    ): IDetailsViewModel = DetailsViewModel(navigation, repository, autoDisposable, imageRepo)


    @Provides
    @FragmentScoped
    fun getListViewModel(
        navigation: INavigation,
        repository: IImageListRepository,
        autoDisposable: AutoDisposable,
        imageRepo: IImageRepository
    ): IImageListViewModel = ImageListViewModel(navigation, repository, autoDisposable, imageRepo)

}