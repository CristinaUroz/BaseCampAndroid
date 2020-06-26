package com.basecamp.android.data.datasources.ddbb.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.common.extensions.safeCall
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.datasources.ddbb.mappers.InfoMapper
import com.basecamp.android.data.datasources.ddbb.mappers.NewsMapper
import com.basecamp.android.data.datasources.ddbb.models.InfoDTO
import com.basecamp.android.data.datasources.ddbb.models.NewsDTO
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Injectable(Scope.BY_USE)
class DDBBDataSourceImpl(private val auth: FirebaseFirestore) : DDBBDataSource {


    private val newsMapper = NewsMapper()
    private val infoMapper = InfoMapper()

    companion object {
        const val USERS = "users"
        const val FAMILIES = "families"
        const val MAFIA = "mafia"
        const val NEWS = "news"
        const val INFO = "info"
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


    override suspend fun getAllUsers(): ResponseState<List<User>> =
        safeCall {
            suspendCoroutine<List<User>> { cont ->
                auth.collection(USERS)
                    .get()
                    .addOnSuccessListener { doc ->
                        cont.resume(doc.mapNotNull { it.toObject(User::class.java) })
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }



    override suspend fun getFamilies(): ResponseState<List<Family>> =
        safeCall {
            suspendCoroutine<List<Family>> { cont ->
                auth.collection(FAMILIES)
                    .get()
                    .addOnSuccessListener { doc ->
                        cont.resume(doc.mapNotNull { it.toObject(Family::class.java) })
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }


    override suspend fun getMafiaWelcome(): ResponseState<MafiaWelcome> =
        safeCall {
            suspendCoroutine<MafiaWelcome> { cont ->
                auth.collection(MAFIA)
                    .get()
                    .addOnSuccessListener { doc ->
                        doc.first().toObject(MafiaWelcome::class.java).let{
                            cont.resume(it)
                        }
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }



    override suspend fun createNews(news: News): ResponseState<News> =
        safeCall {
            suspendCoroutine<News> { cont ->
                auth.collection(NEWS)
                    .add(newsMapper.map(news))
                    .addOnSuccessListener {
                        cont.resume(
                            News(
                                it.id,
                                title = news.title,
                                text = news.text,
                                author = news.author,
                                timestamp = news.timestamp,
                                mafia = news.mafia,
                                picture = news.picture
                            )
                        )
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun updateNews(news: News): ResponseState<Void> =
        safeCall {
            suspendCoroutine<Void> { cont ->
                news.id?.let {
                    auth.collection(NEWS)
                        .document(it)
                        .update(newsMapper.map(news).convert())
                        .addOnSuccessListener {
                            cont.resume(it)
                        }
                        .addOnFailureListener { error -> cont.resumeWithException(error) }
                } ?: cont.resumeWithException(Throwable("Error updating the news"))
            }

        }

    override suspend fun deleteNews(id: String): ResponseState<Void> =
        safeCall {
            suspendCoroutine<Void> { cont ->
                auth.collection(NEWS)
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getNews(id: String): ResponseState<News> =
        safeCall {
            suspendCoroutine<News> { cont ->
                auth.collection(NEWS)
                    .document(id)
                    .get()
                    .addOnSuccessListener {
                        it.toObject(NewsDTO::class.java)?.let { news ->
                            cont.resume(newsMapper.map(news, id))
                        } ?: cont.resumeWithException(Throwable("Error converting to the User object"))
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getAllNews(): ResponseState<List<News>> =
        safeCall {
            suspendCoroutine<List<News>> { cont ->
                auth.collection(NEWS)
                    .get()
                    .addOnSuccessListener { cont.resume(it.getNews()) }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getNormalNews(): ResponseState<List<News>> =
        safeCall {
            suspendCoroutine<List<News>> { cont ->
                auth.collection(NEWS)
                    .whereEqualTo("mafia", false)
                    .get()
                    .addOnSuccessListener { cont.resume(it.getNews()) }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getMafiaNews(): ResponseState<List<News>> =
        safeCall {
            suspendCoroutine<List<News>> { cont ->
                auth.collection(NEWS)
                    .whereEqualTo("mafia", true)
                    .get()
                    .addOnSuccessListener { cont.resume(it.getNews()) }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    private fun QuerySnapshot.getNews(): List<News> =
        documents.mapNotNull { doc ->
            doc.toObject(NewsDTO::class.java)?.let { news ->
                newsMapper.map(news, doc.id)
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
                        Log.i("CRIS", "EXCEPTION DELETING $it")
                    }
            }
        }

    override suspend fun createInfo(info: Info): ResponseState<Info> =
        safeCall {
            suspendCoroutine<Info> { cont ->
                auth.collection(INFO)
                    .add(infoMapper.map(info))
                    .addOnSuccessListener {
                        cont.resume(
                            Info(
                                it.id,
                                title = info.title,
                                text = info.text,
                                mafia = info.mafia
                            )
                        )
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun updateInfo(info: Info): ResponseState<Void> =
        safeCall {
            suspendCoroutine<Void> { cont ->
                info.id?.let {
                    auth.collection(INFO)
                        .document(it)
                        .update(infoMapper.map(info).convert())
                        .addOnSuccessListener {
                            cont.resume(it)
                        }
                        .addOnFailureListener { error -> cont.resumeWithException(error) }
                } ?: cont.resumeWithException(Throwable("Error updating the information"))
            }

        }

    override suspend fun deleteInfo(id: String): ResponseState<Void> =
        safeCall {
            suspendCoroutine<Void> { cont ->
                auth.collection(INFO)
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getInfo(id: String): ResponseState<Info> =
        safeCall {
            suspendCoroutine<Info> { cont ->
                auth.collection(INFO)
                    .document(id)
                    .get()
                    .addOnSuccessListener {
                        it.toObject(InfoDTO::class.java)?.let {
                            cont.resume(infoMapper.map(it, id))
                        } ?: cont.resumeWithException(Exception("Error parsing Info"))
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getAllInfo(): ResponseState<List<Info>> =
        safeCall {
            suspendCoroutine<List<Info>> { cont ->
                auth.collection(INFO)
                    .get()
                    .addOnSuccessListener { cont.resume(it.getInfo()) }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getNormalInfo(): ResponseState<List<Info>> =
        safeCall {
            suspendCoroutine<List<Info>> { cont ->
                auth.collection(INFO)
                    .whereEqualTo("mafia", false)
                    .get()
                    .addOnSuccessListener { cont.resume(it.getInfo()) }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun getMafiaInfo(): ResponseState<List<Info>> =
        safeCall {
            suspendCoroutine<List<Info>> { cont ->
                auth.collection(INFO)
                    .whereEqualTo("mafia", true)
                    .get()
                    .addOnSuccessListener { cont.resume(it.getInfo()) }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }


    private fun QuerySnapshot.getInfo(): List<Info> =
        documents.mapNotNull { doc ->
            doc.toObject(InfoDTO::class.java)?.let {
                infoMapper.map(it, doc.id)
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