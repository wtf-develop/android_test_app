package ru.wtfdev.kitty.list

import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.ItemModel
import ru.wtfdev.kitty.utils.INetwork
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface IImageListRepository {
    fun fetchData(
        dataCallback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )

    fun changeOrder(
        item: ItemModel?,
        callback: ((data: List<ItemModel>) -> Unit)? = null
    )
}


class ImageListRepository private constructor() : IImageListRepository {

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
        network.getJsonArray("http://wtf-dev.ru/test/cats.php",
            { jsonStr ->
                mutableList.clear()
                mutableList.addAll(
                    jsonParser.decodeFromString(
                        ListSerializer(ItemModel.serializer()),
                        jsonStr
                    )
                )

                changeOrder(selectedItem)
                callback(
                    mutableList
                )
            }, { text ->
                errorCallback?.let { it(text) }
            })
    }

    override fun changeOrder(item: ItemModel?, callback: ((data: List<ItemModel>) -> Unit)?) {
        selectedItem = item
        selectedItem?.let { itemSel ->
            val list = mutableListOf<ItemModel>()
            list.addAll(mutableList.filter { listitem ->
                listitem.id.equals(itemSel.id, true)
            })
            list.addAll(mutableList.filter { listitem ->
                !listitem.id.equals(itemSel.id, true)
            })
            mutableList.clear()
            mutableList.addAll(list)
            callback?.let { call ->
                call(mutableList)
            }
        }
    }

    init {
        DaggerComponent.create().inject(this)
    }

    companion object {
        private val repo = ImageListRepository()
        fun getInstance(): IImageListRepository {
            return repo
        }
    }
}