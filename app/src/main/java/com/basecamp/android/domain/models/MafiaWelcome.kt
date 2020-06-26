package com.basecamp.android.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MafiaWelcome(

    @SerializedName("picture")
    var picture: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("text")
    var text: String? = null,

    @SerializedName("video")
    var video: String? = null

): Serializable