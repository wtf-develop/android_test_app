package ru.wtfdev.kitty.detail.implementation


import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty.detail.IDetailsRepository


//Implementation
class DetailsRepository : IDetailsRepository {

    lateinit var itemData: ItemModel

    override fun fetchData(
        dataCallback: (data: ItemModel) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        dataCallback(itemData)
    }

    override fun setParameter(item: ItemModel) {
        itemData = item
    }


}