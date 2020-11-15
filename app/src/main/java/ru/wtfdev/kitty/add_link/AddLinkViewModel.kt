package ru.wtfdev.kitty.add_link

import android.content.Intent
import android.widget.ImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.repo.IImageRepository
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty.utils.AutoDisposable
import javax.inject.Inject


interface IAddLinkViewModel {
    fun save(url: String)
    fun close()
    fun getUrlFromIntent(intent: Intent?): String
    fun loadImageTo(img: ImageView, url: String)
    fun subscribeOnChange(callback: (data: Boolean) -> Unit)
    fun subscribeOnError(callback: (error: String) -> Unit)
    fun unsubscribeAll()

}


class AddLinkViewModel(val navigation: INavigation) : IAddLinkViewModel {
    init {
        DaggerComponent.create().inject(this)
    }

    private val data = BehaviorSubject.create<Boolean>()
    private val error = PublishSubject.create<String>()

    @Inject
    lateinit var repository: IAddLinkRepository

    @Inject
    lateinit var autoDisposable: AutoDisposable

    @Inject
    lateinit var imageRepo: IImageRepository

    override fun save(url: String) {
        repository.postLinkToServer(url, { result ->
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
            // Update UI to reflect text being shared
            parsed = it
        }
        return parsed
    }


    override fun loadImageTo(img: ImageView, url: String) {
        imageRepo.loadImageTo(img, url, 300)
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

    override fun unsubscribeAll() {
        autoDisposable.disconnectAllListeners()
    }


    companion object {
        fun getInstance(navi: INavigation): IAddLinkViewModel =
            AddLinkViewModel(navi)
    }
}