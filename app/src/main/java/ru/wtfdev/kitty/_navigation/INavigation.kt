package ru.wtfdev.kitty._navigation

import ru.wtfdev.kitty._models.data.ItemModel

//common navigation interface
interface INavigation {
    fun push(tag: String?, backstack: Boolean = true): IBaseFragment?
    fun pop(auto: Boolean=false)
    fun getTitle(): String
}
