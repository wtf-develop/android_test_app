package ru.wtfdev.kitty._dagger

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import ru.wtfdev.kitty._models.INetwork
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INaviJump
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty._navigation.Navigation
import ru.wtfdev.kitty.list.IImageListRepository
import ru.wtfdev.kitty.list.IImageListViewModel
import ru.wtfdev.kitty.list.ImageListRepository
import ru.wtfdev.kitty.list.ImageListViewModel
import ru.wtfdev.kitty.utils.AutoDisposable


@Module
@InstallIn(ActivityComponent::class)
class ActivityModules {



    @Provides
    @ActivityScoped
    fun getINavi(act: Activity): INaviJump = act as INaviJump

    @Provides
    @ActivityScoped
    fun getNavigationObject(maniputator: INaviJump): INavigation = Navigation(maniputator)

}