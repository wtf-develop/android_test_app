package ru.wtfdev.kitty.add_link

import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._models.data.PostUrlObject
import ru.wtfdev.kitty._models.data.ServerBaseResponse
import ru.wtfdev.kitty.utils.INetwork
import javax.inject.Inject


interface IAddLinkRepository {
    fun postLinkToServer(
        url: String, title: String, callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    )
}


class AddLinkRepository : IAddLinkRepository {
    init {
        DaggerComponent.create().inject(this)
    }

    @Inject
    lateinit var network: INetwork

    override fun postLinkToServer(
        url: String,
        title: String,
        callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    ) {
        network.postImageUrl(PostUrlObject(url, title), { response ->
            callback(response)
        }, { error ->
            errorCallback?.let { it(error) }
        })
    }


    companion object {
        fun getInstance(): IAddLinkRepository {
            return AddLinkRepository()
        }
    }
}