package com.basecamp.android.data.datasources.ddbb.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.common.extensions.safeCall
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Injectable(Scope.BY_USE)
class DDBBDataSourceImpl(private val auth: FirebaseFirestore) : DDBBDataSource {

    companion object {
        const val USERS = "users"
    }

    override suspend fun createUser(id: String, user: User) =
        safeCall {
            suspendCoroutine<String> { cont ->
                auth.collection(USERS)
                    .add(user).addOnSuccessListener {
                        cont.resume(it.id)
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

}