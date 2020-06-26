package com.basecamp.android.data.datasources.ddbb.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class NewsDTO(

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

)