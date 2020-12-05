package ru.wtfdev.kitty._navigation

import android.os.Bundle

//common navigation interface
interface INavigation {
    fun push(tag: String?, data: Bundle? = null, backstack: Boolean = true): IBaseFragment?
    fun pop(auto: Boolean = false)
    fun getTitle(): String
}
