package com.basecamp.android.data.repositories.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.User
import com.basecamp.android.domain.repositories.DDBBRepository

@Injectable(Scope.BY_USE)
class DDBBRepositoryImpl (private val ddbbDataSource: DDBBDataSource): DDBBRepository {

    override suspend fun getUser(email: String): ResponseState<User> = ddbbDataSource.getUser(email)

}