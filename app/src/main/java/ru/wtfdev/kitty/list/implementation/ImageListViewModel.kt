package ru.wtfdev.kitty.list.implementation

import android.widget.ImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.list.IImageListRepository
import ru.wtfdev.kitty.list.IImageListViewModel
import ru.wtfdev.kitty.utils.AutoDisposable

//Implementation
class ImageListViewModel(
    val navigation: INavigation,
    val repository: IImageListRepository,
    val autoDisposable: AutoDisposable,
    val imageRepo: IImageRepository,
    val storage: ILocalStorageRepository
) : //ViewModel(),
    IImageListViewModel {


    private val data = BehaviorSubject.create<List<ItemModel>>()
    private val error = PublishSubject.create<String>()

    override fun updateList(force: Boolean) {
        val temp = storage.getDailyList()
        if ((temp == null) || (temp?.isEmpty()) || force) {
            repository.fetchData({ arr ->
                data.onNext(arr)
            }, { text ->
                error.onNext(text)
            })
        } else {
            data.onNext(temp)
        }
    }

    override fun subscribeOnChange(callback: (data: List<ItemModel>) -> Unit) {
        autoDisposable.add(
            data.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun subscribeOnError(callback: (error: String) -> Unit) {
        autoDisposable.add(
            error.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun unsubscribeAll() {
        autoDisposable.disconnectAllListeners()
    }

    override fun selectItem(item: ItemModel) {
        navigation.toDetails(item)
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