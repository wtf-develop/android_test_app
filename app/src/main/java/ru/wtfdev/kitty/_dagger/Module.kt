package ru.wtfdev.kitty._dagger

import ru.wtfdev.kitty.detail.DetailsRepository
import ru.wtfdev.kitty.detail.IDetailsRepository
import ru.wtfdev.kitty.list.IImageListRepository
import ru.wtfdev.kitty.list.ImageListRepository
import ru.wtfdev.kitty.utils.INetwork
import ru.wtfdev.kitty.utils.Network
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
object Module {

    @Provides
    fun getNetwork(): INetwork = Network.getInstance()

    @Provides
    fun getListRepo(): IImageListRepository = ImageListRepository.getInstance()

    @Provides
    fun getDetailsRepo(): IDetailsRepository = DetailsRepository.getInstance()

    @Provides
    fun getJsonProcessor(): Json {
        return Json { ignoreUnknownKeys = true }
    }


}