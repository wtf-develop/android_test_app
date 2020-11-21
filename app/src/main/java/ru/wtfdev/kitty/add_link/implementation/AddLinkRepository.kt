package ru.wtfdev.kitty.add_link.implementation


import ru.wtfdev.kitty._models.data.PostUrlObject
import ru.wtfdev.kitty._models.data.ServerBaseResponse
import ru.wtfdev.kitty._models.network.INetwork
import ru.wtfdev.kitty.add_link.IAddLinkRepository


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