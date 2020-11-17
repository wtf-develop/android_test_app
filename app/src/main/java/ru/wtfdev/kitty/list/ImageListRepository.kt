package ru.wtfdev.kitty.list

import ru.wtfdev.kitty._models.INetwork
import ru.wtfdev.kitty._models.data.ItemModel

interface IImageListRepository {
    fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )
}


class ImageListRepository(val network: INetwork) : IImageListRepository {

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
}