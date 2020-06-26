package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.models.Info
import com.basecamp.android.domain.repositories.DDBBRepository

@Injectable(Scope.BY_USE)
class UpdateInfoUseCase(private val ddbbRepository: DDBBRepository) : UseCase {

    suspend fun createInfo(info: Info): ResponseState<Info> = ddbbRepository.createInfo(info)

    suspend fun updateInfo(info: Info): ResponseState<Void> = ddbbRepository.updateInfo(info)

    suspend fun deleteInfo(id: String): ResponseState<Void> = ddbbRepository.deleteInfo(id)

}