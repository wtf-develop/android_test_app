package ru.wtfdev.kitty.list.implementation

import android.os.Bundle
import android.widget.ImageView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.serialization.json.Json
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.list.IImageListRepository
import ru.wtfdev.kitty.list.IImageListViewModel

//Implementation
class ImageListViewModel @ViewModelInject constructor(
    val navigation: INavigation,
    val repository: IImageListRepository,
    val imageRepo: IImageRepository,
    val storage: ILocalStorageRepository,
    val parser: Json
) : ViewModel(),
    IImageListViewModel {


    private val data =
        MutableLiveData<List<ItemModel>>() //BehaviorSubject.create<List<ItemModel>>()
    private val error = MutableLiveData<String>() //PublishSubject.create<String>()

    override fun updateList(force: Boolean) {
        val temp = storage.getDailyList()
        if ((temp == null) || (temp.isEmpty()) || force) {
            repository.fetchData({ arr ->
                data.value = arr
            }, { text ->
                error.value = text
            })
        } else {
            data.value = temp
        }
    }

    lateinit var lifecycle: LifecycleOwner
    override fun setLifeCycle(lifeC: LifecycleOwner) {
        lifecycle = lifeC
    }


    override fun subscribeOnChange(callback: (data: List<ItemModel>) -> Unit) {
        data.observe(lifecycle) {
            callback(it)
        }
    }

    override fun subscribeOnError(callback: (error: String) -> Unit) {
        error.observe(lifecycle) {
            callback(it)
        }
    }

    override fun selectItem(item: ItemModel) {
        val bundle = Bundle()
        bundle.putString("JSON", parser.encodeToString(ItemModel.serializer(), item))
        navigation.push("/details", data = bundle)
    }

    override fun loadImageTo(img: ImageView, url: String) {
        imageRepo.loadImageTo(img, url, 400)
    }

    override fun like(id: Int): Boolean {
        val b = storage.toggleLike(id)
        repository.like(id, if (b) 1 else -1, {})
        return b
    }

    override fun dislike(id: Int): Boolean {
        val b = storage.toggleDislike(id)
        repository.dislike(id, if (b) 1 else -1, {})
        return b
    }

    override fun abuse(id: Int): Boolean {
        val b = storage.toggleAbuse(id)
        repository.abuse(id, if (b) 1 else -1, {})
        return b
    }

    override fun checkLike(id: Int): Boolean {
        return storage.checkLike(id)
    }

    override fun checkDislike(id: Int): Boolean {
        return storage.checkDislike(id)
    }

    override fun checkAbuse(id: Int): Boolean {
        return storage.checkAbuse(id)
    }

}