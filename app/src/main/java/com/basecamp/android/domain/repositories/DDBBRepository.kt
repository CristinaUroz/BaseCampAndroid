package com.basecamp.android.domain.repositories

import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.models.User

interface DDBBRepository {

    suspend fun getUser(email: String): ResponseState<User>

}