package com.basecamp.android.data.repositories.datasources

import com.basecamp.android.data.datasources.DataSource
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.models.News
import com.basecamp.android.domain.models.User

interface DDBBDataSource : DataSource {

    suspend fun createUser(id: String, user: User): ResponseState<Void>

    suspend fun getUser(email: String): ResponseState<User>

    suspend fun updateUser(user: User): ResponseState<Void>

    suspend fun createNews(news: News): ResponseState<News>

    suspend fun updateNews(news: News): ResponseState<Void>

    suspend fun deleteNews(id: String): ResponseState<Void>

    suspend fun getNews(id: String): ResponseState<News>

    suspend fun getAllNews(): ResponseState<List<News>>

    suspend fun getNormalNews(): ResponseState<List<News>>

    suspend fun getMafiaNews(): ResponseState<List<News>>

    suspend fun uploadImage(src: String, name: String): ResponseState<String>

    suspend fun deleteImage(name: String): ResponseState<Void>

}