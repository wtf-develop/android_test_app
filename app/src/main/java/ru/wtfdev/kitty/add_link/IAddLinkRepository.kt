package ru.wtfdev.kitty.add_link

import ru.wtfdev.kitty._models.data.ServerBaseResponse

interface IAddLinkRepository {
    fun postLinkToServer(
        url: String, title: String, callback: (data: ServerBaseResponse) -> Unit,
        errorCallback: ((text: String) -> Unit)?
    )
}
