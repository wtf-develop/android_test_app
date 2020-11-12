package ru.wtfdev.kitty.utils

import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import com.android.volley.*
import com.android.volley.toolbox.*
import ru.wtfdev.kitty.R
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory

/**
 * can be replaced in any implementation. Only interface is used
 */

//Interface
//Interface
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
//Implementation
//Implementation
class Network @Inject constructor() : INetwork {

    val queue: RequestQueue by lazy {
        Volley.newRequestQueue(
            MyApp.ctx,
            MyHurlStack()
        )
    }
    val imageLoader: ImageLoader by lazy { ImageLoader(queue, BitmapCache.getInstance()) }

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
        val curUrl = img.tag as? String
        if (curUrl == null || (!url.equals(curUrl, false))) {
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



    /**
     * A [BaseHttpStack] based on [HttpURLConnection].
     */
    class MyHurlStack constructor(
        private val mUrlRewriter: UrlRewriter? =  /* urlRewriter = */null,
        private val mSslSocketFactory: SSLSocketFactory? =  /* sslSocketFactory = */null
    ) : BaseHttpStack() {
        interface MyUrlRewriter {
            /**
             * Returns a URL to use instead of the provided one, or null to indicate this URL should not be
             * used at all.
             */
            fun rewriteUrl(originalUrl: String?): String?
        }

        /**
         * An interface for transforming URLs before use.
         */
        interface UrlRewriter : MyUrlRewriter

        @Throws(IOException::class, AuthFailureError::class)
        override fun executeRequest(
            request: Request<*>,
            additionalHeaders: Map<String, String>
        ): HttpResponse {
            var url = request.url
            var keepConnectionOpen = false
            var connection: HttpURLConnection? = null
            HttpURLConnection.setFollowRedirects(true)
            HttpsURLConnection.setFollowRedirects(true)
            return try {
                var protect = MAX_REDIRECTIONS //hardcoded max redirections
                var responseCode = -1
                do {
                    protect--
                    if (protect < 1) break
                    mUrlRewriter?.let {
                        url = it.rewriteUrl(url) ?: throw IOException("URL blocked by rewriter: $url")
                    }
                    val map = HashMap<String, String>()
                    map.putAll(additionalHeaders)
                    // Request.getHeaders() takes precedence over the given additional (cache) headers).
                    map.putAll(request.headers)
                    val parsedUrl = URL(url)
                    connection = openConnection(parsedUrl, request)
                    connection.instanceFollowRedirects = true
                    for (headerName in map.keys) {
                        connection.setRequestProperty(headerName, map[headerName])
                    }
                    setConnectionParametersForRequest(connection, request)
                    // Initialize HttpResponse with data from the HttpURLConnection.
                    HttpURLConnection.setFollowRedirects(true)
                    connection.instanceFollowRedirects = true
                    responseCode = connection.responseCode
                    if (responseCode == -1) {
                        // -1 is returned by getResponseCode() if the response code could not be retrieved.
                        // Signal to the caller that something was wrong with the connection.
                        throw IOException("Could not retrieve response code from HttpUrlConnection.")
                    }
                    if (responseCode == 307 || responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                        val newUrl = connection.getHeaderField("Location")
                        connection.disconnect()
                        url = newUrl
                        continue
                    } else {
                        break
                    }
                } while (true)
                if (connection == null) throw IOException("Connection internal error.")
                if (!hasResponseBody(request.method, responseCode)) {
                    return HttpResponse(responseCode, convertHeaders(connection.headerFields))
                }

                // Need to keep the connection open until the stream is consumed by the caller. Wrap the
                // stream such that close() will disconnect the connection.
                keepConnectionOpen = true
                HttpResponse(
                    responseCode,
                    convertHeaders(connection.headerFields),
                    connection.contentLength,
                    createInputStream(request, connection)
                )
            } finally {
                if (!keepConnectionOpen) {
                    connection!!.disconnect()
                }
            }
        }

        /**
         * Wrapper for a [HttpURLConnection]'s InputStream which disconnects the connection on
         * stream close.
         */
        internal class UrlConnectionInputStream(private val mConnection: HttpURLConnection) :
            FilterInputStream(
                inputStreamFromConnection(
                    mConnection
                )
            ) {
            @Throws(IOException::class)
            override fun close() {
                super.close()
                mConnection.disconnect()
            }
        }

        /**
         * Create and return an InputStream from which the response will be read.
         *
         *
         * May be overridden by subclasses to manipulate or monitor this input stream.
         *
         * @param request    current request.
         * @param connection current connection of request.
         * @return an InputStream from which the response will be read.
         */
        protected fun createInputStream(
            request: Request<*>?,
            connection: HttpURLConnection
        ): InputStream {
            return UrlConnectionInputStream(connection)
        }

        /**
         * Create an [HttpURLConnection] for the specified `url`.
         */
        @Throws(IOException::class)
        protected fun createConnection(url: URL): HttpURLConnection {
            val connection = url.openConnection() as HttpURLConnection

            // Workaround for the M release HttpURLConnection not observing the
            // HttpURLConnection.setFollowRedirects() property.
            // https://code.google.com/p/android/issues/detail?id=194495
            connection.instanceFollowRedirects = HttpURLConnection.getFollowRedirects()
            return connection
        }

        /**
         * Opens an [HttpURLConnection] with parameters.
         *
         * @param url
         * @return an open connection
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun openConnection(url: URL, request: Request<*>): HttpURLConnection {
            val connection = createConnection(url)
            val timeoutMs = request.timeoutMs
            connection.connectTimeout = timeoutMs
            connection.readTimeout = timeoutMs
            connection.useCaches = false
            connection.doInput = true

            // use caller-provided custom SslSocketFactory, if any, for HTTPS
            if ("https" == url.protocol && mSslSocketFactory != null) {
                (connection as HttpsURLConnection).sslSocketFactory = mSslSocketFactory
            }
            return connection
        }

        // NOTE: Any request headers added here (via setRequestProperty or addRequestProperty) should be
        // checked against the existing properties in the connection and not overridden if already set.
        @Throws(IOException::class, AuthFailureError::class)
        fun  /* package */setConnectionParametersForRequest(
            connection: HttpURLConnection?, request: Request<*>
        ) {
            when (request.method) {
                Request.Method.DEPRECATED_GET_OR_POST -> {
                    // This is the deprecated way that needs to be handled for backwards compatibility.
                    // If the request's post body is null, then the assumption is that the request is
                    // GET.  Otherwise, it is assumed that the request is a POST.
                    val postBody = request.postBody
                    if (postBody != null) {
                        connection!!.requestMethod = "POST"
                        addBody(connection, request, postBody)
                    }
                }
                Request.Method.GET ->                 // Not necessary to set the request method because connection defaults to GET but
                    // being explicit here.
                    connection!!.requestMethod = "GET"
                Request.Method.DELETE -> connection!!.requestMethod = "DELETE"
                Request.Method.POST -> {
                    connection!!.requestMethod = "POST"
                    addBodyIfExists(connection, request)
                }
                Request.Method.PUT -> {
                    connection!!.requestMethod = "PUT"
                    addBodyIfExists(connection, request)
                }
                Request.Method.HEAD -> connection!!.requestMethod = "HEAD"
                Request.Method.OPTIONS -> connection!!.requestMethod = "OPTIONS"
                Request.Method.TRACE -> connection!!.requestMethod = "TRACE"
                Request.Method.PATCH -> {
                    connection!!.requestMethod = "PATCH"
                    addBodyIfExists(connection, request)
                }
                else -> throw IllegalStateException("Unknown method type.")
            }
        }

        @Throws(IOException::class, AuthFailureError::class)
        private fun addBodyIfExists(connection: HttpURLConnection?, request: Request<*>) {
            val body = request.body
            body?.let { addBody(connection, request, it) }
        }

        @Throws(IOException::class)
        private fun addBody(connection: HttpURLConnection?, request: Request<*>, body: ByteArray) {
            // Prepare output. There is no need to set Content-Length explicitly,
            // since this is handled by HttpURLConnection using the size of the prepared
            // output stream.
            connection!!.doOutput = true
            // Set the content-type unless it was already set (by Request#getHeaders).
            if (!connection.requestProperties.containsKey("Content-Type")) {
                connection.setRequestProperty(
                    "Content-Type", request.bodyContentType
                )
            }
            val out = DataOutputStream(createOutputStream(request, connection, body.size))
            out.write(body)
            out.close()
        }

        /**
         * Create and return an OutputStream to which the request body will be written.
         *
         *
         * May be overridden by subclasses to manipulate or monitor this output stream.
         *
         * @param request    current request.
         * @param connection current connection of request.
         * @param length     size of stream to write.
         * @return an OutputStream to which the request body will be written.
         * @throws IOException if an I/O error occurs while creating the stream.
         */
        @Throws(IOException::class)
        protected fun createOutputStream(
            request: Request<*>?, connection: HttpURLConnection?, length: Int
        ): OutputStream {
            return connection!!.outputStream
        }

        companion object {
            private const val HTTP_CONTINUE = 100
            private const val MAX_REDIRECTIONS = 3
            @VisibleForTesting
            fun convertHeaders(responseHeaders: Map<String?, List<String?>>): List<Header> {
                val headerList: MutableList<Header> = ArrayList(responseHeaders.size)
                for ((key, value1) in responseHeaders) {
                    // HttpUrlConnection includes the status line as a header with a null key; omit it here
                    // since it's not really a header and the rest of Volley assumes non-null keys.
                    if (key != null) {
                        for (value in value1) {
                            headerList.add(Header(key, value))
                        }
                    }
                }
                return headerList
            }

            /**
             * Checks if a response message contains a body.
             *
             * @param requestMethod request method
             * @param responseCode  response status code
             * @return whether the response has a body
             * @see [RFC 7230 section 3.3](https://tools.ietf.org/html/rfc7230.section-3.3)
             */
            private fun hasResponseBody(requestMethod: Int, responseCode: Int): Boolean {
                return (requestMethod != Request.Method.HEAD && !(HTTP_CONTINUE <= responseCode && responseCode < HttpURLConnection.HTTP_OK)
                        && responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_NOT_MODIFIED)
            }

            /**
             * Initializes an [InputStream] from the given [HttpURLConnection].
             *
             * @param connection
             * @return an HttpEntity populated with data from `connection`.
             */
            private fun inputStreamFromConnection(connection: HttpURLConnection): InputStream {
                val inputStream: InputStream
                inputStream = try {
                    connection.inputStream
                } catch (ioe: IOException) {
                    connection.errorStream
                }
                return inputStream
            }
        }
        /**
         * @param mUrlRewriter      Rewriter to use for request URLs
         * @param mSslSocketFactory SSL factory to use for HTTPS connections
         */
    }


    companion object {
        val single = Network()
        fun getInstance(): INetwork = single
    }
}