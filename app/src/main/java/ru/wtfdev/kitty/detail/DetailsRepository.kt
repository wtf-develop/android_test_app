package ru.wtfdev.kitty.detail

import ru.wtfdev.kitty._dagger.DaggerComponent
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

    init {
        DaggerComponent.create().inject(this)
    }

    override fun fetchData(
        dataCallback: (data: ItemModel) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        dataCallback(itemData)
    }


    companion object {
        lateinit var itemData: ItemModel
        fun getInstance(): IDetailsRepository {
            return DetailsRepository()
        }
    }
}