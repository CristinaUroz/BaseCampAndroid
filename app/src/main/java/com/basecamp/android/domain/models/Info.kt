package com.basecamp.android.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Info(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("text")
    var text: String? = null,

    @SerializedName("mafia")
    var mafia: Boolean = false

): Serializable