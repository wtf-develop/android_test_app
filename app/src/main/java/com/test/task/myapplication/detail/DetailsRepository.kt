package com.test.task.myapplication.detail

import com.test.task.myapplication._models.ItemModel

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
        lateinit var itemData: ItemModel
        fun getInstance(): IDetailsRepository {
            return DetailsRepository()
        }
    }
}