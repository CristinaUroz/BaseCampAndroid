package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.models.News
import com.basecamp.android.domain.repositories.DDBBRepository

@Injectable(Scope.BY_USE)
class GetNewsUseCase(private val ddbbRepository: DDBBRepository) : UseCase {

    suspend fun getNews(id: String): ResponseState<News> = ddbbRepository.getNews(id)

    suspend fun getAllNews(): ResponseState<List<News>> = ddbbRepository.getAllNews()

    suspend fun getNormalNews(): ResponseState<List<News>> = ddbbRepository.getNormalNews()

    suspend fun getMafiaNews(): ResponseState<List<News>> = ddbbRepository.getMafiaNews()

}