package ru.wtfdev.kitty._models.repo

import ru.wtfdev.kitty._models.data.ItemModel

interface ILocalStorageRepository {
    fun getUUID(): String
    fun storeDailyList(listdata: List<ItemModel>)
    fun getDailyList(): List<ItemModel>?

    fun toggleLike(id: Int): Boolean
    fun toggleDislike(id: Int): Boolean
    fun toggleAbuse(id: Int): Boolean

    fun checkLike(id: Int): Boolean
    fun checkDislike(id: Int): Boolean
    fun checkAbuse(id: Int): Boolean

}
