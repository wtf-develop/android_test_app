package ru.wtfdev.kitty.detail

import android.widget.ImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.utils.AutoDisposable


//Interface
interface IDetailsViewModel {
    fun update(force: Boolean = false)
    fun subscribeOnChange(callback: (data: ItemModel) -> Unit)
    fun unsubscribeAll()
    fun loadImageTo(img: ImageView, url: String)
    fun close()
}

//Implementation
class DetailsViewModel(
    val navigation: INavigation,
    val repository: IDetailsRepository,
    val autoDisposable: AutoDisposable,
    val imageRepo: IImageRepository
) : IDetailsViewModel {


    private val data = BehaviorSubject.create<ItemModel>()


    override fun update(force: Boolean) {
        repository.fetchData({ item ->
            data.onNext(item)
        })
    }

    override fun subscribeOnChange(callback: (data: ItemModel) -> Unit) {
        autoDisposable.add(
            data.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun unsubscribeAll() {
        autoDisposable.disconnectAllListeners()
    }


    override fun loadImageTo(img: ImageView, url: String) {
        imageRepo.loadImageTo(img, url, 1000)
    }

    override fun close() {
        navigation.popBackStack()
    }

}