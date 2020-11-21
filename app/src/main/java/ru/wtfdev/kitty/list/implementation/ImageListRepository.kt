package ru.wtfdev.kitty.list.implementation

import ru.wtfdev.kitty._models.INetwork
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.data.ServerBaseResponse
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import ru.wtfdev.kitty.list.IImageListRepository


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
                callback(mutableList)
            }, { text ->
                errorCallback?.let { it(text) }
            })
    }

    override fun like(
        id: Int,
        value: Int,
        callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        network.like(id, value,
            { json ->
                callback(json)
            }, { text ->
                errorCallback?.let { it(text) }
            })
    }

    override fun dislike(
        id: Int,
        value: Int,
        callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        network.dislike(id, value,
            { json ->
                callback(json)
            }, { text ->
                errorCallback?.let { it(text) }
            })
    }

    override fun abuse(
        id: Int,
        value: Int,
        callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        network.abuse(id, value,
            { json ->
                callback(json)
            }, { text ->
                errorCallback?.let { it(text) }
            })
    }


}