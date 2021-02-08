package com.saifkhichi.app.data.source

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.saifkhichi.app.db.remote.WebService
import com.saifkhichi.app.model.Result
import com.saifkhichi.app.model.User
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication with login credentials and retrieves
 * user information from the Firebase backend.
 *
 * @author saifkhichi96
 */
class LoginDataSource @Inject constructor(var webService: WebService) {

    private val auth get() = Firebase.auth

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
            webService.getUser(uid)
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