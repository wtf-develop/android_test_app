package ru.wtfdev.kitty._models.data

import kotlinx.serialization.Serializable

@Serializable
data class PostUrlObject(val url: String, val title: String)