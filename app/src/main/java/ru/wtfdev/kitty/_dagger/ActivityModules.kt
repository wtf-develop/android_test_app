package ru.wtfdev.kitty._dagger

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty._navigation.INavigationProvider
import ru.wtfdev.kitty._navigation.implementation.Navigation


@Module
@InstallIn(ActivityComponent::class)
class ActivityModules {

    @Provides
    @ActivityScoped
    fun getINavi(act: Activity): INavigationProvider = act as INavigationProvider

    @Provides
    @ActivityScoped
    fun getNavigationObject(provider: INavigationProvider, ctx: Context): INavigation =
        Navigation(provider, ctx)

}