package ru.wtfdev.kitty._navigation

import ru.wtfdev.kitty._models.data.ItemModel

//common navigation interface
interface INavigation {
    fun moveTo(tag: String?, backstack: Boolean = true): IBaseFragment?
    fun popBackStack()

    fun toList()
    fun toDetails(obj: ItemModel)

    fun getTitle(tag: String): Int
}
