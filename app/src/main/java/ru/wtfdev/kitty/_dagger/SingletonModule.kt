package ru.wtfdev.kitty._dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import ru.wtfdev.kitty._models.network.INetwork
import ru.wtfdev.kitty._models.network.implementation.Network
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import ru.wtfdev.kitty._models.repo.implementation.ImageRepository
import ru.wtfdev.kitty._models.repo.implementation.LocalStorageRepository
import ru.wtfdev.kitty.list.IImageListRepository
import ru.wtfdev.kitty.list.implementation.ImageListRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun getNetwork(localrepo: ILocalStorageRepository, jsonParser: Json): INetwork =
        Network(localrepo, jsonParser)

    @Provides
    @Singleton
    fun getLocalStorage(ctx: Context, parser: Json): ILocalStorageRepository =
        LocalStorageRepository(ctx, parser)

    @Provides
    @Singleton
    fun getBaseContext(app: Application): Context = app.baseContext

    @Provides
    @Singleton
    fun imageRepo(net: INetwork): IImageRepository = ImageRepository(net)


    @Provides
    @Singleton
    fun getListRepo(network: INetwork, storage: ILocalStorageRepository): IImageListRepository =
        ImageListRepository(network, storage)


    @Provides
    @Singleton
    fun getJsonProcessor(): Json {
        return Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
        }
    }
}