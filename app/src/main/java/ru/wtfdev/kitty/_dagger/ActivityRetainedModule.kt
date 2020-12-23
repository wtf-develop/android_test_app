package ru.wtfdev.kitty._dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.FragmentScoped
import ru.wtfdev.kitty._models.network.INetwork
import ru.wtfdev.kitty.add_link.IAddLinkRepository
import ru.wtfdev.kitty.add_link.implementation.AddLinkRepository
import ru.wtfdev.kitty.utils.AutoDisposable


@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityRetainedModule {

    @Provides
    @ActivityRetainedScoped
    fun getAddLink(network: INetwork): IAddLinkRepository = AddLinkRepository(network)

    @Provides
    @ActivityRetainedScoped
    fun getAutoDisposable(): AutoDisposable = AutoDisposable()


}