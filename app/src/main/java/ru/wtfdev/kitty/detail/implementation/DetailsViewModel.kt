package ru.wtfdev.kitty.detail.implementation

import android.widget.ImageView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.serialization.json.Json
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.detail.IDetailsRepository
import ru.wtfdev.kitty.detail.IDetailsViewModel


//Implementation
class DetailsViewModel @ViewModelInject constructor(
    val navigation: INavigation,
    val repository: IDetailsRepository,
    val imageRepo: IImageRepository,
    val parser: Json
) : IDetailsViewModel, ViewModel() {


    private val data = MutableLiveData<ItemModel>()//BehaviorSubject.create<ItemModel>()


    override fun update(force: Boolean) {
        repository.fetchData({ item ->
            data.value = item
        })
    }

    override fun subscribeOnChange(callback: (data: ItemModel) -> Unit) {
        data.observe(lifecycle) {
            callback(it)
        }
    }

    lateinit var lifecycle: LifecycleOwner
    override fun setLifeCycle(lifeC: LifecycleOwner) {
        lifecycle = lifeC
    }

    override fun loadImageTo(img: ImageView, url: String) {
        imageRepo.loadImageTo(img, url, 1000)
    }

    override fun close() {
        navigation.pop()
    }

    override fun setDataString(json: String) {
        repository.setParameter(parser.decodeFromString(ItemModel.serializer(), json))
    }

}