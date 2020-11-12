package ru.wtfdev.kitty.detail

import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.ItemModel

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


    init {
        DaggerComponent.create().inject(this)
    }

    companion object {
        lateinit var itemData: ItemModel
        fun getInstance(): IDetailsRepository {
            return DetailsRepository()
        }
    }
}