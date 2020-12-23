package ru.wtfdev.kitty.detail

import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import ru.wtfdev.kitty._models.data.ItemModel

//Interface
interface IDetailsViewModel {
    fun update(force: Boolean = false)
    fun subscribeOnChange(callback: (data: ItemModel) -> Unit)
    fun setLifeCycle(lifeC: LifecycleOwner)
    fun loadImageTo(img: ImageView, url: String)
    fun close()
    fun setDataString(json: String)
}
