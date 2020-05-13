package com.basecamp.android.domain.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("group")
    val group: Int? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("alias")
    val alias: String? = null,

    @SerializedName("adult")
    val adult: Boolean? = null
)