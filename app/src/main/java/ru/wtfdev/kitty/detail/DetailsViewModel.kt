package ru.wtfdev.kitty.detail

import android.widget.ImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.ItemModel
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.utils.AutoDisposable
import ru.wtfdev.kitty.utils.Network
import javax.inject.Inject


//Interface
interface IDetailsViewModel {
    fun update(force: Boolean = false)
    fun subscribeOnChange(callback: (data: ItemModel) -> Unit)
    fun unsubscribeAll()
    fun loadImageTo(img: ImageView, url: String)
}

//Implementation
class DetailsViewModel(val navigation: INavigation) : IDetailsViewModel {
    @Inject
    lateinit var repository: IDetailsRepository

    private val data = BehaviorSubject.create<ItemModel>()
    private var autoDisposable: AutoDisposable = AutoDisposable()


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
        Network.getInstance().setImageMainThread(img, url, 1000)
    }


    init {
        DaggerComponent.create().inject(this)
    }


    companion object {
        fun getInstance(navigationObj: INavigation): IDetailsViewModel {
            return DetailsViewModel(navigationObj)
        }
    }
}