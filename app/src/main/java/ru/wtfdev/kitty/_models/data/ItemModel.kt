package ru.wtfdev.kitty._models.data

import kotlinx.serialization.Serializable

@Serializable
data class ItemModel(
    val id: Int,
    val link: String,
    val title: String = "",
    val likes: Int = 0,
    var dislakes: Int = 0
)

