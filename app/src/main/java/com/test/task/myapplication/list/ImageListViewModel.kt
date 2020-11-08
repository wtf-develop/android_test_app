package com.test.task.myapplication.list

import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import com.test.task.myapplication.INavigation
import com.test.task.myapplication._dagger.DaggerComponent
import com.test.task.myapplication._models.ItemModel
import com.test.task.myapplication.detail.DetailsRepository
import com.test.task.myapplication.utils.AutoDisposable
import com.test.task.myapplication.utils.INetwork
import com.test.task.myapplication.utils.Network
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

//Interface
interface IImageListViewModel {
    fun updateList()
    fun subscribeOnChange(callback: (data: List<ItemModel>) -> Unit)
    fun subscribeOnError(callback: (error: String) -> Unit)
    fun setLifecycle(lifecycle: Lifecycle)
    fun selectItem(item: ItemModel)
    fun loadImageTo(img: ImageView, url: String)
}


//Implementation
class ImageListViewModel private constructor() :
    IImageListViewModel {

    @Inject
    lateinit var network: INetwork
    @Inject
    lateinit var repository: IImageListRepository

    private lateinit var navigation: INavigation
    private val data = BehaviorSubject.create<List<ItemModel>>()
    private val error = PublishSubject.create<String>()
    private var autoDisposable: AutoDisposable? = null

    override fun updateList() {
        repository.fetchData({ arr ->
            data.onNext(arr)
        }, { text ->
            error.onNext(text)
        })
    }

    override fun subscribeOnChange(callback: (data: List<ItemModel>) -> Unit) {
        if (autoDisposable == null) throw Exception("run setLifecycle first")
        autoDisposable?.add(
            data.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun subscribeOnError(callback: (error: String) -> Unit) {
        if (autoDisposable == null) throw Exception("run setLifecycle first")
        autoDisposable?.add(
            error.observeOn(AndroidSchedulers.mainThread())
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

    override fun selectItem(item: ItemModel) {
        DetailsRepository.itemData = item
        repository.changeOrder(item) { arr ->
            data.onNext(arr)
        }
        navigation.toDetails()
    }

    override fun loadImageTo(img: ImageView, url: String) {
        network.setImageMainThread(img, url, 400)
    }


    init {
        DaggerComponent.create().inject(this)
    }

    companion object {
        private val listViewModel = ImageListViewModel()
        fun getInstance(navigation: INavigation): IImageListViewModel {
            listViewModel.navigation = navigation
            return listViewModel
        }
    }
}