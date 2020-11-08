package com.test.task.myapplication.list

import android.widget.ImageView
import com.test.task.myapplication.INavigation
import com.test.task.myapplication._models.ItemModel
import com.test.task.myapplication.utils.AutoDisposable
import com.test.task.myapplication.utils.INetwork
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

//Interface
interface IImageListViewModel {
    fun updateList()
    fun subscribeOnChange(callback: (data: List<ItemModel>) -> Unit)
    fun subscribeOnError(callback: (error: String) -> Unit)
    fun loadImageTo(img: ImageView, url: String)
    fun setLifecycle(autoDisposable: AutoDisposable)
    fun selectItem(item: ItemModel)
}


//Implementation
class ImageListViewModel private constructor() :
    IImageListViewModel {
    private val data = BehaviorSubject.create<List<ItemModel>>()
    private val error = PublishSubject.create<String>()
    private var autoDisposable: AutoDisposable? = null
    private lateinit var network: INetwork
    private lateinit var navigation: INavigation
    private lateinit var jsonParser: Json
    override fun updateList() {
        network.getJsonArray("http://wtf-dev.ru/test/cats.php",
            { jsonStr ->
                data.onNext(
                    jsonParser.decodeFromString(
                        ListSerializer(ItemModel.serializer()),
                        jsonStr
                    )
                )
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

    override fun loadImageTo(img: ImageView, url: String) {
        network.setImageMainThread(img, url, 400)
    }

    override fun setLifecycle(lifecycle: AutoDisposable) {
        autoDisposable = lifecycle
    }

    override fun selectItem(item: ItemModel) {
        navigation.toDetails()
    }

    companion object {
        private val listViewModel = ImageListViewModel()
        fun getInstance(
            network: INetwork,
            navigation: INavigation,
            jsonParser: Json
        ): IImageListViewModel {
            listViewModel.network = network
            listViewModel.navigation = navigation
            listViewModel.jsonParser = jsonParser
            return listViewModel
        }
    }
}