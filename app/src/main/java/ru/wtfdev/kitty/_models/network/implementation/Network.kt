package ru.wtfdev.kitty._models.network.implementation

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.http.*
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.data.PostUrlObject
import ru.wtfdev.kitty._models.data.ServerBaseResponse
import ru.wtfdev.kitty._models.network.INetwork
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository


/**
 * can be replaced in any implementation. Only interface is used
 */

//Interface
//Interface


//Implementation
//Implementation
//Implementation
class Network(val localRepo: ILocalStorageRepository, val jsonConverter: Json) : INetwork {

    interface APIService {
        @GET("cats.php")
        fun loadList(@Header("Installation") uuid: String): Observable<List<ItemModel>>

        @POST("new_cat.php")
        fun postImage(
            @Header("Installation") uuid: String,
            @Body obj: PostUrlObject
        ): Observable<ServerBaseResponse>

        @GET("like.php")
        fun updateLike(
            @Header("Installation") uuid: String,
            @Query("id") id: Int,
            @Query("val") change: Int
        ): Observable<ServerBaseResponse>

        @GET("dislike.php")
        fun updateDislike(
            @Header("Installation") uuid: String,
            @Query("id") id: Int,
            @Query("val") change: Int
        ): Observable<ServerBaseResponse>

        @GET("abuse.php")
        fun updateAbuse(
            @Header("Installation") uuid: String,
            @Query("id") id: Int,
            @Query("val") change: Int
        ): Observable<ServerBaseResponse>

    }


    @kotlinx.serialization.ExperimentalSerializationApi
    val service = Retrofit.Builder()
        .baseUrl("https://wtf-dev.ru/kitty/") //http://192.168.178.22/kitty/
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(jsonConverter.asConverterFactory(MediaType.get("application/json; charset=utf-8")))
        .build()
        .create(APIService::class.java)

    @kotlinx.serialization.ExperimentalSerializationApi
    override fun getJsonArray(
        onData: (json: List<ItemModel>) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {
        service.loadList(localRepo.getUUID()).subscribe({ response ->
            onData(response)

        }, { error ->
            onError?.let { it(error.toString()) }

        })
    }

    //todo need to catch redirection urls
    override fun setImageMainThread(
        img: ImageView,
        url: String,
        maxSize: Int,
        success: ((url: String?) -> Unit)?,
        error: (() -> Unit)?
    ) {

        Glide
            .with(img.context)
            .load(url)
            .override(maxSize, maxSize)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    error?.let { it() }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    success?.let {
                        it(model as? String)
                    }
                    return false
                }
            })
            .placeholder(R.drawable.loading_img)
            .error(android.R.drawable.ic_delete)
            .into(img)
    }

    override fun like(
        id: Int,
        value: Int,
        onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {
        service.updateLike(localRepo.getUUID(), id, value).subscribe({ response ->
            onData(response)
        }, { error ->
            onError?.let { it(error.toString()) }
        })
    }

    override fun dislike(
        id: Int,
        value: Int,
        onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {
        service.updateDislike(localRepo.getUUID(), id, value).subscribe({ response ->
            onData(response)
        }, { error ->
            onError?.let { it(error.toString()) }
        })
    }

    override fun abuse(
        id: Int,
        value: Int,
        onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {
        service.updateAbuse(localRepo.getUUID(), id, value).subscribe({ response ->
            onData(response)
        }, { error ->
            onError?.let { it(error.toString()) }
        })
    }

    @kotlinx.serialization.ExperimentalSerializationApi
    override fun postImageUrl(
        data: PostUrlObject, onData: (json: ServerBaseResponse) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {
        service.postImage(localRepo.getUUID(), data).subscribe({ response ->
            onData(response)
        }, { error ->
            onError?.let { it(error.toString()) }
        })
    }
}