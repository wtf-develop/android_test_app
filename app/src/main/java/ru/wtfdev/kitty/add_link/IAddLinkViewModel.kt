package ru.wtfdev.kitty.add_link

import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import ru.wtfdev.kitty._models.data.ServerDone

interface IAddLinkViewModel {
    fun saveLoadedImageUrl(title: String)
    fun close()
    fun getUrlFromIntent(intent: Intent?): String
    fun loadImageTo(img: ImageView, url: String)
    fun subscribeOnChange(callback: (data: ServerDone) -> Unit)
    fun subscribeOnError(callback: (error: String) -> Unit)
    fun subscribeOnImageSuccess(callback: (b: Boolean) -> Unit)
    fun extractUrl(text: String?): String
    fun getUrlFlow(
        intent: Intent?,
        savedInstanceState: Bundle?,
        clipboard: ClipboardManager?
    ): String

    fun saveUrlState(outState: Bundle, url: String): Bundle
    fun setLifeCycle(lifeC: LifecycleOwner)

}
