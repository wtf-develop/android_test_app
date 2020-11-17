package ru.wtfdev.kitty.add_link


import ru.wtfdev.kitty._models.INetwork
import ru.wtfdev.kitty._models.data.PostUrlObject
import ru.wtfdev.kitty._models.data.ServerBaseResponse


interface IAddLinkRepository {
    fun postLinkToServer(
        url: String, title: String, callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    )
}


class AddLinkRepository(val network: INetwork) : IAddLinkRepository {

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


}