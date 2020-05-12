package com.basecamp.android.domain.models

data class User (
    val name: String,
    val email: String,
    val image: String? = null,
    val group: Int? = null,
    val description: String? = null,
    val alias: String? = null,
    val adult: Boolean? = null
)