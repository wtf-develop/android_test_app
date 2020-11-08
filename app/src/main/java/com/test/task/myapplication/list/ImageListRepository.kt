package com.test.task.myapplication.list

import com.test.task.myapplication._models.ItemModel
import com.test.task.myapplication.utils.INetwork
import com.test.task.myapplication.utils.Network
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

interface IImageListRepository {
    fun fetchData(
        dataCallback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )
}


class ImageListRepository private constructor() : IImageListRepository {
    private val jsonParser: Json= Json { ignoreUnknownKeys=true }
    val network: INetwork = Network.getInstance()
    override fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        network.getJsonArray("http://wtf-dev.ru/test/cats.php",
            { jsonStr ->
                callback(
                    jsonParser.decodeFromString(
                        ListSerializer(ItemModel.serializer()),
                        jsonStr
                    )
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