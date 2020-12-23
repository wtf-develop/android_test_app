package ru.wtfdev.kitty.add_link.implementation

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.wtfdev.kitty._models.data.ServerDone
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.add_link.IAddLinkRepository
import ru.wtfdev.kitty.add_link.IAddLinkViewModel
import ru.wtfdev.kitty.utils.StringUtils

/// Here i will test LiveData module
class AddLinkViewModel @ViewModelInject constructor(
    val navigation: INavigation,
    val repository: IAddLinkRepository,
    val imageRepo: IImageRepository,
    @Assisted private val savedStateHandle: SavedStateHandle

) : IAddLinkViewModel, ViewModel() {

    private val data = MutableLiveData<ServerDone>() //BehaviorSubject.create<ServerDone>()
    private val error = MutableLiveData<String>() //PublishSubject.create<String>()
    private val imageSuccess = MutableLiveData<Boolean>() //PublishSubject.create<Boolean>()
    lateinit var lifecycle: LifecycleOwner

    override fun setLifeCycle(lifeC: LifecycleOwner) {
        lifecycle = lifeC
    }


    override fun saveLoadedImageUrl(title: String) {
        if ((!canSaveImage2Server) || (successImageUrl.isEmpty())) {
            error.value = "Wrong image"
            return
        }
        repository.postLinkToServer(successImageUrl, title, { result ->
            if (result.done.state) {
                data.value = result.done
                navigation.pop()
            } else if (result.error.state) {
                error.value = result.error.message
            } else {
                error.value = "Server error"
            }
        }, { errorstr ->
            error.value = errorstr
        })
    }

    override fun close() {
        navigation.pop()
    }

    override fun getUrlFromIntent(intent: Intent?): String {
        intent?.let { data ->
            if ("text/plain" == data.type) {
                return getTextUrl(data)
            }
        }
        return ""
    }

    fun getTextUrl(intent: Intent): String {
        var parsed = ""
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            parsed = extractUrl(it)
        }
        return parsed
    }

    override fun extractUrl(text: String?): String {
        return StringUtils.extractImgUrl(text)
    }

    private val URL_KEY = "img_url"
    override fun getUrlFlow(
        intent: Intent?,
        savedInstanceState: Bundle?,
        clipboard: ClipboardManager?
    ): String {
        var url = savedInstanceState?.getString(URL_KEY, "") ?: ""
        if (url.isEmpty()) {
            url = getUrlFromIntent(intent)
        }
        if (url.isEmpty()) {//clipboard
            clipboard?.let { clip ->
                if (clip.hasPrimaryClip() && (true == clip.primaryClipDescription?.hasMimeType(
                        ClipDescription.MIMETYPE_TEXT_PLAIN
                    ))
                ) {
                    val item = clip.primaryClip?.getItemAt(0)
                    url = extractUrl(item?.text?.toString())
                }
            }
        }
        return extractUrl(url)
    }

    override fun saveUrlState(outState: Bundle, url: String): Bundle {
        outState.putString(URL_KEY, extractUrl(url))
        return outState
    }


    private var canSaveImage2Server = false
    private var successImageUrl: String = ""
    override fun loadImageTo(img: ImageView, url: String) {
        canSaveImage2Server = false
        successImageUrl = ""
        imageRepo.loadImageTo(img, url, 300, success = { lasturl ->
            canSaveImage2Server = true
            successImageUrl = lasturl ?: ""
            imageSuccess.value = true
        })


    }

    override fun subscribeOnChange(callback: (data: ServerDone) -> Unit) {
        data.observe(lifecycle) {
            callback(it)
        }
    }

    override fun subscribeOnError(callback: (error: String) -> Unit) {
        error.observe(lifecycle) {
            callback(it)
        }
    }

    override fun subscribeOnImageSuccess(callback: (b: Boolean) -> Unit) {
        imageSuccess.observe(lifecycle) {
            callback(it)
        }
    }

}