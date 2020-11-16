package ru.wtfdev.kitty._models.data

import kotlinx.serialization.Serializable

@Serializable
data class ServerBaseResponse(
    val error: ServerError = ServerError(),
    val done: ServerDone = ServerDone()
)

@Serializable
data class ServerError(
    val state: Boolean = false,
    val message: String = "",
    val title: String = "",
    val code: Int = 0
)

@Serializable
data class ServerDone(val state: Boolean = false, val message: String = "")