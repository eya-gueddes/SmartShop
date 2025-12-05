package com.eya.smartshop.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    fun currentUser(): FirebaseUser? = auth.currentUser

    suspend fun signIn(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun signUp(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password).await()

    fun signOut() = auth.signOut()
}
