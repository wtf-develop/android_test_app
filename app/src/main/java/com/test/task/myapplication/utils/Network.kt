package com.test.task.myapplication.utils

import android.widget.ImageView
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.test.task.myapplication.R
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection


//Interface
interface INetwork {
    fun getJsonArray(
        url: String,
        onData: (jsonStr: String) -> Unit,
        onError: ((text: String) -> Unit)? = null
    )

    fun setImageMainThread(img: ImageView, url: String, maxSize: Int)
}


//Implementation
class Network private constructor() : INetwork {

    val queue: RequestQueue by lazy { Volley.newRequestQueue(MyApp.ctx, MyHurlStack()) }
    val imageLoader: ImageLoader by lazy { ImageLoader(queue, LruBtimapCache.getInstance()) }

    override fun getJsonArray(
        url: String,
        onData: (jsonStr: String) -> Unit,
        onError: ((text: String) -> Unit)?
    ) {
        HttpURLConnection.setFollowRedirects(true)
        HttpsURLConnection.setFollowRedirects(true)

        val jsonRequest = StringRequest(
            url,
            { response ->
                onData(response)
            },
            { error ->
                onError?.let { it(error?.toString() ?: "Network error") }
            }
        )
        queue.add(jsonRequest)
    }

    override fun setImageMainThread(img: ImageView, url: String, maxSize: Int) {
        HttpURLConnection.setFollowRedirects(true)
        HttpsURLConnection.setFollowRedirects(true)
        val curUrl=img.tag as? String
        if (curUrl == null||(!url.equals(curUrl,false))) {
            img.setImageResource(R.drawable.loading_img)
        }
        img.tag = url

        imageLoader.get(url, object : ImageLoader.ImageListener {
            override fun onErrorResponse(error: VolleyError?) {
                if (url.equals(img.tag.toString(), true)) {
                    img.setImageResource(android.R.drawable.ic_delete)//TODO may be need to cache error response image
                }
            }

            override fun onResponse(response: ImageLoader.ImageContainer?, isImmediate: Boolean) {
                if (url.equals(img.tag.toString(), true)) {
                    response?.bitmap?.let {
                        img.setImageBitmap(it)
                    }
                }
            }

        }, maxSize, maxSize, ImageView.ScaleType.CENTER)
    }


    companion object {
        val single = Network()
        fun getInstance(): INetwork = single
    }
}