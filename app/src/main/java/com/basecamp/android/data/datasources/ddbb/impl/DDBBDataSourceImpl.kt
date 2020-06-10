package com.basecamp.android.data.datasources.ddbb.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.common.extensions.safeCall
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
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
                user.email?.let {
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


    override suspend fun uploadImage(src: String, name: String): ResponseState<String> =
        safeCall {
            suspendCoroutine<String> { cont ->
                val bitmap = BitmapFactory.decodeFile(src)
                val baos = ByteArrayOutputStream()
                val storageRef = FirebaseStorage.getInstance().reference.child(name)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                storageRef.putBytes(baos.toByteArray())
                    .addOnCompleteListener { uploadTask ->
                        if (uploadTask.isSuccessful) {
                            storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                                urlTask.result?.let {
                                    cont.resume(it.toString())
                                }
                            }
                        } else {
                            uploadTask.exception?.let {
                                cont.resumeWithException(it)
                            }
                        }

                    }
            }
        }


    override suspend fun deleteImage(name: String): ResponseState<Void> =
        safeCall {
            suspendCoroutine<Void> { cont ->
                val storageRef = FirebaseStorage.getInstance().reference.child(name)
                storageRef.delete()
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
                    .addOnFailureListener {
                        cont.resumeWithException(it)
                        Log.i("CRIS","EXCEPTION DELETING $it")
                    }
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