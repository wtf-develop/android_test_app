package ru.wtfdev.kitty.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.http.GET
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.data.ItemModel
import javax.inject.Inject


/**
 * can be replaced in any implementation. Only interface is used
 */

//Interface
//Interface
//Interface
interface INetwork {
    fun getJsonArray(
        onData: (json: List<ItemModel>) -> Unit,
        onError: ((text: String) -> Unit)? = null
    )

    fun setImageMainThread(img: ImageView, url: String, maxSize: Int)
}


//Implementation
//Implementation
//Implementation
class Network @Inject constructor() : INetwork {
    init {
        DaggerComponent.create().inject(this)
    }

    interface APIService {
        @GET("cats.php")
        fun loadList(): Observable<List<ItemModel>>
    }


    @Inject
    lateinit var jsonConverter: Json

    @kotlinx.serialization.ExperimentalSerializationApi
    val service = Retrofit.Builder()
        .baseUrl("https://wtf-dev.ru/test/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(jsonConverter.asConverterFactory(MediaType.get("application/json; charset=utf-8")))
        .build()
        .create(APIService::class.java)

    override fun getJsonArray(
        onData: (json: List<ItemModel>) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {
        service.loadList().subscribe({ response ->
            onData(response)

        }, { error ->
            onError?.let { it(error.toString()) }

        })
    }

    override fun setImageMainThread(img: ImageView, url: String, maxSize: Int) {
        Glide
            .with(img.context)
            .load(url)
            .placeholder(R.drawable.loading_img)
            .error(android.R.drawable.ic_delete)
            .into(img)
    }


    companion object {
        val single = Network()
        fun getInstance(): INetwork = single
    }
}