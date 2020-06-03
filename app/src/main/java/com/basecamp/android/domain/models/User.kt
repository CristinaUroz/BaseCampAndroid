package com.basecamp.android.domain.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("imageM")
    var imageM: String? = null,

    @SerializedName("group")
    var group: Int? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("descriptionM")
    var descriptionM: String? = null,

    @SerializedName("alias")
    var alias: String? = null,

    @SerializedName("adult")
    var adult: Boolean? = null
)