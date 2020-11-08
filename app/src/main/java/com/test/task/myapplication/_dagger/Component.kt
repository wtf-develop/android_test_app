package com.test.task.myapplication._dagger

import com.test.task.myapplication.detail.DetailsRepository
import com.test.task.myapplication.detail.DetailsView
import com.test.task.myapplication.detail.DetailsViewModel
import com.test.task.myapplication.list.ImageListRepository
import com.test.task.myapplication.list.ImageListView
import com.test.task.myapplication.list.ImageListViewModel
import dagger.Binds
import dagger.Component

@Component(modules = [Module::class])
interface Component {

    fun inject(obj: ImageListRepository)
    fun inject(obj: ImageListViewModel)
    fun inject(obj: ImageListView)
    fun inject(obj: DetailsRepository)
    fun inject(obj: DetailsViewModel)
    fun inject(obj: DetailsView)

}