package com.basecamp.android.data.repositories.impl

import android.util.Log
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.User
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
            Log.i("CRIS", "FIREBASE STORAGE IMAGE M")
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

    private fun String.isInFirebaseStorage(): Boolean = this.startsWith("https://firebasestorage.googleapis.com/")

    private fun createProfilePicName(name: String): String {
        return "profile/$name"
    }

    private fun createProfilePicMName(name: String): String {
        return "profileM/$name"
    }

}