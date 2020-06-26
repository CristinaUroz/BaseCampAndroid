package com.basecamp.android.data.repositories.impl

import android.util.Log
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.*
import com.basecamp.android.domain.repositories.DDBBRepository

@Injectable(Scope.BY_USE)
class DDBBRepositoryImpl(private val ddbbDataSource: DDBBDataSource) : DDBBRepository {

    override suspend fun getUser(email: String): ResponseState<User> = ddbbDataSource.getUser(email)

    override suspend fun updateUser(user: User): ResponseState<Void> {
        var newImage: String? = null
        var newImageM: String? = null
        user.image?.takeIf { it != "" && !it.isInFirebaseStorage() }?.let {
            Log.i("CRIS", "FIREBASE STORAGE IMAGE")
            val response = ddbbDataSource.uploadImage(it, createProfilePicName(user.email ?: "error"))
            if (response is ResponseState.Success) {
                newImage = response.result
            }
        } ?: user.image?.takeIf { it == "" }?.let {
            ddbbDataSource.deleteImage(createProfilePicName(user.email ?: "error"))
            newImage = ""
        }

        user.imageM?.takeIf { it != "" && !it.isInFirebaseStorage() }?.let {
            val response = ddbbDataSource.uploadImage(it, createProfilePicMName(user.email ?: "error"))
            if (response is ResponseState.Success) {
                newImageM = response.result
            }
        } ?: user.imageM?.takeIf { it == "" }?.let {
            ddbbDataSource.deleteImage(createProfilePicMName(user.email ?: "error"))
            newImageM = ""
        }

        user.image = newImage
        user.imageM = newImageM

        Log.i("CRIS", "USER = $user")
        return ddbbDataSource.updateUser(user)
    }

    override suspend fun getAllUsers(): ResponseState<List<User>> = ddbbDataSource.getAllUsers()


    override suspend fun getFamilies(): ResponseState<List<Family>> = ddbbDataSource.getFamilies()


    override suspend fun getMafiaWelcome(): ResponseState<MafiaWelcome> = ddbbDataSource.getMafiaWelcome()


    override suspend fun createNews(news: News): ResponseState<News> =
        ddbbDataSource.createNews(news).also { response ->
            if (response is ResponseState.Success) {
                response.result.let { if (it.picture != null) updateNews(it) }
            }
        }

    override suspend fun updateNews(news: News): ResponseState<Void> {
        var newImage: String? = null
        news.picture?.takeIf { it != "" && !it.isInFirebaseStorage() }?.let {
            val response = ddbbDataSource.uploadImage(it, createNewsPicName(news.id ?: "error"))
            if (response is ResponseState.Success) {
                newImage = response.result
            }
        } ?: news.picture?.takeIf { it == "" }?.let {
            ddbbDataSource.deleteImage(createNewsPicName(news.id ?: "error"))
            newImage = ""
        }
        news.picture = newImage
        return ddbbDataSource.updateNews(news)
    }

    override suspend fun deleteNews(id: String): ResponseState<Void> {
        ddbbDataSource.deleteImage(createNewsPicName(id))
        return ddbbDataSource.deleteNews(id)
    }

    override suspend fun getNews(id: String): ResponseState<News> = ddbbDataSource.getNews(id)

    override suspend fun getAllNews(): ResponseState<List<News>> = ddbbDataSource.getAllNews()

    override suspend fun getNormalNews(): ResponseState<List<News>> = ddbbDataSource.getNormalNews()

    override suspend fun getMafiaNews(): ResponseState<List<News>> = ddbbDataSource.getMafiaNews()


    override suspend fun createInfo(info: Info): ResponseState<Info> = ddbbDataSource.createInfo(info)

    override suspend fun updateInfo(info: Info): ResponseState<Void> = ddbbDataSource.updateInfo(info)

    override suspend fun deleteInfo(id: String): ResponseState<Void> = ddbbDataSource.deleteInfo(id)

    override suspend fun getInfo(id: String): ResponseState<Info> = ddbbDataSource.getInfo(id)

    override suspend fun getAllInfo(): ResponseState<List<Info>> = ddbbDataSource.getAllInfo()

    override suspend fun getNormalInfo(): ResponseState<List<Info>> = ddbbDataSource.getNormalInfo()

    override suspend fun getMafiaInfo(): ResponseState<List<Info>> = ddbbDataSource.getMafiaInfo()


    private fun String.isInFirebaseStorage(): Boolean = this.startsWith("https://firebasestorage.googleapis.com/")

    private fun createProfilePicName(name: String): String {
        return "profile/$name"
    }

    private fun createNewsPicName(name: String): String {
        return "news/$name"
    }

    private fun createProfilePicMName(name: String): String {
        return "profileM/$name"
    }

}