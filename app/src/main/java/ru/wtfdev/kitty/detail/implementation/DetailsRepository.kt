package ru.wtfdev.kitty.detail.implementation


import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty.detail.IDetailsRepository


//Implementation
class DetailsRepository : IDetailsRepository {


    override fun fetchData(
        dataCallback: (data: ItemModel) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        dataCallback(itemData)
    }


    companion object {
        lateinit var itemData: ItemModel //todo - Yes-yes, i know!!! Later!
    }
}