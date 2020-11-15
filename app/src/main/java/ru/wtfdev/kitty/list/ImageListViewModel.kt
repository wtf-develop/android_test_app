package ru.wtfdev.kitty.list

import android.widget.ImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.utils.AutoDisposable
import javax.inject.Inject

//Interface
interface IImageListViewModel {
    fun updateList(force: Boolean = false)
    fun subscribeOnChange(callback: (data: List<ItemModel>) -> Unit)
    fun subscribeOnError(callback: (error: String) -> Unit)
    fun unsubscribeAll()
    fun selectItem(item: ItemModel)
    fun loadImageTo(img: ImageView, url: String)
}


//Implementation
class ImageListViewModel private constructor(val navigation: INavigation) : //ViewModel(),
    IImageListViewModel {

    init {
        DaggerComponent.create().inject(this)
    }

    @Inject
    lateinit var repository: IImageListRepository

    @Inject
    lateinit var autoDisposable: AutoDisposable

    @Inject
    lateinit var imageRepo: IImageRepository


    private val data = BehaviorSubject.create<List<ItemModel>>()
    private val error = PublishSubject.create<String>()

    override fun updateList(force: Boolean) {
        repository.fetchData({ arr ->
            data.onNext(arr)
        }, { text ->
            error.onNext(text)
        })
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


    companion object {
        fun getInstance(navi: INavigation): IImageListViewModel =
            ImageListViewModel(navi)
    }
}