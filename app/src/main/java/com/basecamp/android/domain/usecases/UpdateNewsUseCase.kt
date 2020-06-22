package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.models.News
import com.basecamp.android.domain.repositories.DDBBRepository

@Injectable(Scope.BY_USE)
class UpdateNewsUseCase(private val ddbbRepository: DDBBRepository) : UseCase {

    suspend fun createNews(news: News): ResponseState<News> = ddbbRepository.createNews(news)

    suspend fun updateNews(news: News): ResponseState<Void> = ddbbRepository.updateNews(news)

    suspend fun deleteNews(id: String): ResponseState<Void> = ddbbRepository.deleteNews(id)

}