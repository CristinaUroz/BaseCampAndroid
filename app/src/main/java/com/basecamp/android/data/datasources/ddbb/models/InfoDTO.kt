package com.basecamp.android.data.datasources.ddbb.models

import com.google.gson.annotations.SerializedName

data class InfoDTO(

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("text")
    var text: String? = null,

    @SerializedName("mafia")
    var mafia: Boolean = false

)