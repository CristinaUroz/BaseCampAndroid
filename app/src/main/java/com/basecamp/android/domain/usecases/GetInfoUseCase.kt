package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.models.Info
import com.basecamp.android.domain.repositories.DDBBRepository

@Injectable(Scope.BY_USE)
class GetInfoUseCase(private val ddbbRepository: DDBBRepository) : UseCase {

    suspend fun getInfo(id: String): ResponseState<Info> = ddbbRepository.getInfo(id)

    suspend fun getAllInfo(): ResponseState<List<Info>> = ddbbRepository.getAllInfo()

    suspend fun getNormalInfo(): ResponseState<List<Info>> = ddbbRepository.getNormalInfo()

    suspend fun getMafiaInfo(): ResponseState<List<Info>> = ddbbRepository.getMafiaInfo()
}