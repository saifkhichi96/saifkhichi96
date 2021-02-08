package com.saifkhichi.app.data.repo

import com.saifkhichi.app.data.source.LoginDataSource
import com.saifkhichi.app.model.Result
import com.saifkhichi.app.model.User
import javax.inject.Inject

/**
 * Requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class LoginRepository @Inject constructor(var dataSource: LoginDataSource) {

    /**
     * In-memory cache of the currently logged-in user
     */
    var currentUser: User? = null
        private set

    /**
     * Checks if a user is logged in or not
     */
    val isLoggedIn: Boolean
        get() = currentUser != null

    init {
        // TODO: Read logged-in user data from persistent storage
        currentUser = null
    }

    suspend fun login(email: String, password: String): Result<User> {
        val result = dataSource.login(email, password)
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun logout() {
        currentUser = null
        dataSource.logout()
    }

    private fun setLoggedInUser(user: User) {
        this.currentUser = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        // TODO: Persist logged-in user to stay signed in
    }

}