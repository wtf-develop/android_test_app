package ru.wtfdev.kitty.list

import ru.wtfdev.kitty._models.INetwork
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository

interface IImageListRepository {
    fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )
}


class ImageListRepository(val network: INetwork, val storage: ILocalStorageRepository) :
    IImageListRepository {

    val mutableList = mutableListOf<ItemModel>()


    override fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        network.getJsonArray(
            { json ->
                mutableList.clear()
                mutableList.addAll(json)
                storage.storeDailyList(mutableList)
                callback(
                    mutableList
                )
            }, { text ->
                errorCallback?.let { it(text) }
            })
    }
}