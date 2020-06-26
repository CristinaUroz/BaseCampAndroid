package com.basecamp.android.data.repositories.datasources

import com.basecamp.android.data.datasources.DataSource
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.models.*

interface DDBBDataSource : DataSource {

    suspend fun createUser(id: String, user: User): ResponseState<Void>

    suspend fun getUser(email: String): ResponseState<User>

    suspend fun updateUser(user: User): ResponseState<Void>

    suspend fun getAllUsers(): ResponseState<List<User>>


    suspend fun getFamilies(): ResponseState<List<Family>>


    suspend fun getMafiaWelcome(): ResponseState<MafiaWelcome>


    suspend fun createNews(news: News): ResponseState<News>

    suspend fun updateNews(news: News): ResponseState<Void>

    suspend fun deleteNews(id: String): ResponseState<Void>

    suspend fun getNews(id: String): ResponseState<News>

    suspend fun getAllNews(): ResponseState<List<News>>

    suspend fun getNormalNews(): ResponseState<List<News>>

    suspend fun getMafiaNews(): ResponseState<List<News>>


    suspend fun createInfo(info: Info): ResponseState<Info>

    suspend fun updateInfo(info: Info): ResponseState<Void>

    suspend fun deleteInfo(id: String): ResponseState<Void>

    suspend fun getInfo(id: String): ResponseState<Info>

    suspend fun getAllInfo(): ResponseState<List<Info>>

    suspend fun getNormalInfo(): ResponseState<List<Info>>

    suspend fun getMafiaInfo(): ResponseState<List<Info>>


    suspend fun uploadImage(src: String, name: String): ResponseState<String>

    suspend fun deleteImage(name: String): ResponseState<Void>

}