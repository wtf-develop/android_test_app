package ru.wtfdev.kitty.list

import kotlinx.serialization.json.Json
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.ItemModel
import ru.wtfdev.kitty.utils.INetwork
import javax.inject.Inject

interface IImageListRepository {
    fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )
}


class ImageListRepository private constructor() : IImageListRepository {
    init {
        DaggerComponent.create().inject(this)
    }

    @Inject
    lateinit var network: INetwork

    @Inject
    lateinit var jsonParser: Json

    val mutableList = mutableListOf<ItemModel>()
    var selectedItem: ItemModel? = null


    override fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        network.getJsonArray(
            { json ->
                mutableList.clear()
                mutableList.addAll(
                    json
                )

                callback(
                    mutableList
                )
            }, { text ->
                errorCallback?.let { it(text) }
            })
    }

    companion object {
        private val repo = ImageListRepository()
        fun getInstance(): IImageListRepository {
            return repo
        }
    }
}