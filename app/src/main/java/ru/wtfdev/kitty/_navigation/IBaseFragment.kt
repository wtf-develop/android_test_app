package ru.wtfdev.kitty._navigation

//Interface for Navigation object
interface IBaseFragment {
    fun setIsForegroung(b: Boolean)
    fun onSubscribeBindings()
    fun onUnsubscribeBindings()
}
