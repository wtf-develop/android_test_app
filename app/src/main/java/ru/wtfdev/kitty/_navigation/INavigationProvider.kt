package ru.wtfdev.kitty._navigation

import androidx.fragment.app.FragmentManager

//interface for routing fragments
interface INavigationProvider {
    fun getNaviFragmentManager(): FragmentManager?
    fun finish()
}
