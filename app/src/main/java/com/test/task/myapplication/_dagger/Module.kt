package com.test.task.myapplication._dagger

import com.test.task.myapplication.detail.DetailsRepository
import com.test.task.myapplication.detail.IDetailsRepository
import com.test.task.myapplication.list.IImageListRepository
import com.test.task.myapplication.list.ImageListRepository
import com.test.task.myapplication.utils.INetwork
import com.test.task.myapplication.utils.Network
import dagger.Module
import dagger.Provides

@Module
object Module {

    @Provides
    fun getNetwork(): INetwork = Network.getInstance()

    @Provides
    fun getListRepo(): IImageListRepository = ImageListRepository.getInstance()

    @Provides
    fun getDetailsRepo(): IDetailsRepository = DetailsRepository.getInstance()


}