package ru.wtfdev.kitty.detail

import ru.wtfdev.kitty._models.data.ItemModel

//Interface
interface IDetailsRepository {
    fun fetchData(
        dataCallback: (data: ItemModel) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )
}
