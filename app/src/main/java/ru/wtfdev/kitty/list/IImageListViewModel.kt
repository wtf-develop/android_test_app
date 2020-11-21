package ru.wtfdev.kitty.list

import android.widget.ImageView
import ru.wtfdev.kitty._models.data.ItemModel

//Interface
interface IImageListViewModel {
    fun updateList(force: Boolean = false)
    fun subscribeOnChange(callback: (data: List<ItemModel>) -> Unit)
    fun subscribeOnError(callback: (error: String) -> Unit)
    fun unsubscribeAll()
    fun selectItem(item: ItemModel)
    fun loadImageTo(img: ImageView, url: String)
    fun like(id: Int): Boolean
    fun dislike(id: Int): Boolean
    fun abuse(id: Int): Boolean

    fun checkLike(id: Int): Boolean
    fun checkDislike(id: Int): Boolean
    fun checkAbuse(id: Int): Boolean

}
