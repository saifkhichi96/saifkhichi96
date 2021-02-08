package com.saifkhichi.app.model

import java.io.Serializable

/**
 * Data class that captures user information for app users.
 *
 * @property id The unique Firebase user id associated with this user.
 * @property name The name of the user.
 * @property email The email address of the user.
 *
 * @author saifkhichi96
 */
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
) : Serializable