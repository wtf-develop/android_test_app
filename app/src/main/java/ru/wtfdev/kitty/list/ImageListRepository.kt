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
    var requested_time=0L


    override fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        if(mutableList.size<1||(Math.abs(requested_time-System.currentTimeMillis())>60*1000f)) {
            network.getJsonArray(
                { json ->
                    mutableList.clear()
                    mutableList.addAll(
                        json
                    )
                    requested_time=System.currentTimeMillis()
                    callback(
                        mutableList
                    )
                }, { text ->
                    errorCallback?.let { it(text) }
                })
        }else{
            callback(
                mutableList
            )
        }
    }
}