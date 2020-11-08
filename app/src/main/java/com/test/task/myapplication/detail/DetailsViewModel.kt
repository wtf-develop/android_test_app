package com.test.task.myapplication.detail

import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import com.test.task.myapplication.INavigation
import com.test.task.myapplication._models.ItemModel
import com.test.task.myapplication.utils.AutoDisposable
import com.test.task.myapplication.utils.Network
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject


//Interface
interface IDetailsViewModel {
    fun update()
    fun subscribeOnChange(callback: (data: ItemModel) -> Unit)
    fun setLifecycle(lifecycle: Lifecycle)
    fun loadImageTo(img: ImageView, url: String)
}

//Implementation
class DetailsViewModel(val navigation: INavigation) : IDetailsViewModel {
    val repository: IDetailsRepository = DetailsRepository.getInstance()
    private val data = BehaviorSubject.create<ItemModel>()
    private var autoDisposable: AutoDisposable? = null


    override fun update() {
        repository.fetchData({ item ->
            data.onNext(item)
        })
    }

    override fun subscribeOnChange(callback: (data: ItemModel) -> Unit) {
        if (autoDisposable == null) throw Exception("run setLifecycle first")
        autoDisposable?.add(
            data.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun setLifecycle(lifecycle: Lifecycle) {
        autoDisposable = AutoDisposable().apply {
            bindTo(lifecycle)
        }
    }

    override fun loadImageTo(img: ImageView, url: String) {
        Network.getInstance().setImageMainThread(img, url, 1000)
    }

    companion object {
        fun getInstance(navigationObj: INavigation): IDetailsViewModel {
            return DetailsViewModel(navigationObj)
        }
    }
}