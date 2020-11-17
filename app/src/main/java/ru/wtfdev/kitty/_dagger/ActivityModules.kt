package ru.wtfdev.kitty._dagger

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import ru.wtfdev.kitty._navigation.INaviJump
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty._navigation.Navigation


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