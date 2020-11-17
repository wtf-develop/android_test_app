package ru.wtfdev.kitty.detail


import ru.wtfdev.kitty._models.data.ItemModel

//Interface
interface IDetailsRepository {
    fun fetchData(
        dataCallback: (data: ItemModel) -> Unit,
        errorCallback: ((text: String) -> Unit)? = null
    )
}

//Implementation
class DetailsRepository : IDetailsRepository {


    override fun fetchData(
        dataCallback: (data: ItemModel) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        dataCallback(itemData)
    }


    companion object {
        lateinit var itemData: ItemModel //Yes-yes, i know!!! Later!
    }
}