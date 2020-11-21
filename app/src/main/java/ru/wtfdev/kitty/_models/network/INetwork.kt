package ru.wtfdev.kitty._models.network

import android.widget.ImageView
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.data.PostUrlObject
import ru.wtfdev.kitty._models.data.ServerBaseResponse

//Interface
interface INetwork {
    fun getJsonArray(
        onData: (json: List<ItemModel>) -> Unit,
        onError: ((text: String) -> Unit)? = null
    )

    fun postImageUrl(
        data: PostUrlObject,
        onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)? = null
    )

    fun setImageMainThread(
        img: ImageView,
        url: String,
        maxSize: Int,
        success: ((url: String?) -> Unit)? = null,
        error: (() -> Unit)? = null
    )

    fun like(
        id: Int, value: Int,
        onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)? = null
    )

    fun dislike(
        id: Int, value: Int,
        onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)? = null
    )

    fun abuse(
        id: Int, value: Int,
        onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)? = null
    )

}