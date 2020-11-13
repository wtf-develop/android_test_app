package ru.wtfdev.kitty.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.ItemModel
import javax.inject.Inject


/**
 * can be replaced in any implementation. Only interface is used
 */

//Interface
//Interface
//Interface
interface INetwork {
    fun getJsonArray(
        url: String,
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
        fun loadList(): Call<List<ItemModel>>
    }



    @Inject
    lateinit var jsonConverter: Json

    val retrofit = Retrofit.Builder()
        .baseUrl("https://wtf-dev.ru/test/")
        .addConverterFactory(jsonConverter.asConverterFactory(MediaType.get("application/json; charset=utf-8")))
        .build()

    val service = retrofit.create(APIService::class.java)

    override fun getJsonArray(
        url: String,
        onData: (json: List<ItemModel>) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {


        service.loadList().enqueue(object : Callback<List<ItemModel>> {

            override fun onResponse(
                call: Call<List<ItemModel>>,
                response: Response<List<ItemModel>>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    onData(body)
                } else {
                    onError?.let { it(response.errorBody().toString()) }
                }
            }

            override fun onFailure(call: Call<List<ItemModel>>, t: Throwable) {
                onError?.let { it(t.toString()) }
            }

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