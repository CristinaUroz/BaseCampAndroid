package com.basecamp.android.domain.models

import com.google.gson.annotations.SerializedName

data class Family(

    @SerializedName("familyId")
    var familyId: Int = 0,

    @SerializedName("info")
    var info: String? = null

)