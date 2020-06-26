package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.models.Family
import com.basecamp.android.domain.repositories.DDBBRepository

@Injectable(Scope.BY_USE)
class GetFamiliesUseCase(private val ddbbRepository: DDBBRepository) : UseCase {

    suspend fun getFamilies(): ResponseState<List<Family>> = ddbbRepository.getFamilies()

}