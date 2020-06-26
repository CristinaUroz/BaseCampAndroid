package com.basecamp.android.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class News(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("text")
    var text: String? = null,

    @SerializedName("author")
    var author: Int? = null,

    @SerializedName("timestamp")
    var timestamp: Long = Date().time,

    @SerializedName("mafia")
    var mafia: Boolean = false,

    @SerializedName("picture")
    var picture: String? = null

): Serializable