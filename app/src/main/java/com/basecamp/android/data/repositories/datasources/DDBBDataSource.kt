package com.basecamp.android.data.repositories.datasources

import com.basecamp.android.data.datasources.DataSource
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.models.User

interface DDBBDataSource: DataSource {

    suspend fun createUser (id: String, user: User): ResponseState<Void>

    suspend fun getUser (email: String): ResponseState<User>

}