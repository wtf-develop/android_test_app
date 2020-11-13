package ru.wtfdev.kitty._models

import kotlinx.serialization.Serializable

@Serializable
data class ItemModel(
    val id: String,
    val title: String,
    val imageUrl: String,
    var descr: String = ""
)

