package com.saifkhichi.app.auth.data.source

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.saifkhichi.app.auth.model.User
import com.saifkhichi.app.core.model.Result
import io.github.saifkhichi96.android.db.RemoteDatabase
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication with login credentials and retrieves
 * user information from the Firebase backend.
 *
 * @author saifkhichi96
 */
class LoginDataSource @Inject constructor(var db: RemoteDatabase) {

    private val auth get() = Firebase.auth

    suspend fun getUser(uid: String): User? {
        return db.getOrNull("users/$uid")
    }

    /**
     * Signs a user into their account with their login credentials.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     *
     * @return The [Result] containing the signed in [User], or an error message
     */
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Login to authenticate the User
            val authResult = auth
                .signInWithEmailAndPassword(email, password)
                .await()

            // Confirm login success by checking that the User is accessible
            val uid = authResult?.user?.uid ?: throw IOException("Error logging in")

            // Fetch and return User details
            val user = getUser(uid) ?: throw IOException("Error logging in")
            Result.Success(user)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    /**
     * Signs out the current user from their account.
     */
    fun logout() {
        auth.signOut()
    }

}