package ru.wtfdev.kitty.list

import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.data.ServerBaseResponse

interface IImageListRepository {
    fun fetchData(
        callback: (data: List<ItemModel>) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )


    fun like(
        id: Int, value: Int,
        callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )

    fun dislike(
        id: Int, value: Int,
        callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )

    fun abuse(
        id: Int, value: Int,
        callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )

}