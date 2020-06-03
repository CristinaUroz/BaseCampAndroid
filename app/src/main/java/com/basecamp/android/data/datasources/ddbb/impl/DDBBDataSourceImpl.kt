package com.basecamp.android.data.datasources.ddbb.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.common.extensions.safeCall
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Injectable(Scope.BY_USE)
class DDBBDataSourceImpl(private val auth: FirebaseFirestore) : DDBBDataSource {

    companion object {
        const val USERS = "users"
    }

    override suspend fun createUser(email: String, user: User) =
        safeCall {
            suspendCoroutine<Void> { cont ->
                auth.collection(USERS)
                    .document(email)
                    .set(user)
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getUser(email: String): ResponseState<User> =
        safeCall {
            suspendCoroutine<User> { cont ->
                auth.collection(USERS)
                    .document(email)
                    .get()
                    .addOnSuccessListener {
                        it.toObject(User::class.java)?.let { user ->
                            cont.resume(user)
                        } ?: cont.resumeWithException(Throwable("Error converting to the User object"))
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun updateUser(user: User) =
        safeCall {
            suspendCoroutine<Void> { cont ->
                user.email?.let{
                    auth.collection(USERS)
                        .document(it)
                        .update(user.convert())
                        .addOnSuccessListener {
                            cont.resume(it)
                        }
                        .addOnFailureListener { error -> cont.resumeWithException(error) }
                } ?: cont.resumeWithException(Throwable("Error updating the user"))
            }

        }


    val gson = Gson()

    //convert a data class to a map
    fun <T> T.serializeToMap(): Map<String, Any> {
        return convert()
    }

    //convert a map to a data class
    inline fun <reified T> Map<String, Any>.toDataClass(): T {
        return convert()
    }

    //convert an object of type I to type O
    inline fun <I, reified O> I.convert(): O {
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<O>() {}.type)
    }
}