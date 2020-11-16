package ru.wtfdev.kitty.add_link

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.utils.AutoDisposable
import ru.wtfdev.kitty.utils.StringUtils
import javax.inject.Inject


interface IAddLinkViewModel {
    fun saveLoadedImageUrl(title: String)
    fun close()
    fun getUrlFromIntent(intent: Intent?): String
    fun loadImageTo(img: ImageView, url: String)
    fun subscribeOnChange(callback: (data: Boolean) -> Unit)
    fun subscribeOnError(callback: (error: String) -> Unit)
    fun subscribeOnImageSuccess(callback: (b: Boolean) -> Unit)
    fun unsubscribeAll()
    fun extractUrl(text: String?): String
    fun getUrlFlow(
        intent: Intent?,
        savedInstanceState: Bundle?,
        clipboard: ClipboardManager?
    ): String

    fun saveUrlState(outState: Bundle, url: String): Bundle

}


class AddLinkViewModel(val navigation: INavigation) : IAddLinkViewModel {
    init {
        DaggerComponent.create().inject(this)
    }

    private val data = BehaviorSubject.create<Boolean>()
    private val error = PublishSubject.create<String>()
    private val imageSuccess = PublishSubject.create<Boolean>()

    @Inject
    lateinit var repository: IAddLinkRepository

    @Inject
    lateinit var autoDisposable: AutoDisposable

    @Inject
    lateinit var imageRepo: IImageRepository

    override fun saveLoadedImageUrl(title: String) {
        if ((!canSaveImage2Server) || (successImageUrl.isEmpty())) {
            error.onNext("Wrong image")
            return
        }
        repository.postLinkToServer(successImageUrl, title, { result ->
            if (result.status) {
                data.onNext(true)
                navigation.popBackStack()
            } else {
                error.onNext("Server error")
            }
        }, { errorstr ->
            error.onNext(errorstr)
        })
    }

    override fun close() {
        navigation.popBackStack()
    }

    override fun getUrlFromIntent(intnt: Intent?): String {
        intnt?.let { intent ->
            if ("text/plain" == intent.type) {
                return getTextUrl(intent)
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
        imageRepo.loadImageTo(img, url, 300, success = { url ->
            canSaveImage2Server = true
            successImageUrl = url ?: ""
            imageSuccess.onNext(true)
        })


    }

    override fun subscribeOnChange(callback: (data: Boolean) -> Unit) {
        autoDisposable.add(
            data.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun subscribeOnError(callback: (error: String) -> Unit) {
        autoDisposable.add(
            error.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun subscribeOnImageSuccess(callback: (b: Boolean) -> Unit) {
        autoDisposable.add(
            imageSuccess.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback(it)
                }
        )
    }

    override fun unsubscribeAll() {
        autoDisposable.disconnectAllListeners()
    }


    companion object {
        fun getInstance(navi: INavigation): IAddLinkViewModel =
            AddLinkViewModel(navi)
    }
}