package com.basecamp.android.data.datasources.ddbb.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.common.extensions.safeCall
import com.basecamp.android.data.repositories.datasources.AuthDataSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Injectable(Scope.BY_USE)
class AuthDataSourceImpl(private val auth: FirebaseAuth) : AuthDataSource {

    override suspend fun forgotPassword(email: String) =
        safeCall {
            suspendCoroutine<Void?> { cont ->
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }

    override suspend fun logIn(email: String, password: String) =
        safeCall {
            suspendCoroutine<AuthResult?> { cont ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }


    override suspend fun signUp(email: String, password: String) =
        safeCall {
            suspendCoroutine<AuthResult?> { cont ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
                    .addOnFailureListener { error -> cont.resumeWithException(error) }
            }
        }


    override suspend fun logOut() = auth.signOut()


    override suspend fun getCurrentUserMail() = auth.currentUser?.email

}