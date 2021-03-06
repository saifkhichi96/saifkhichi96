package com.saifkhichi.app.db.remote

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.saifkhichi.app.model.Inbox
import com.saifkhichi.app.model.Message
import com.saifkhichi.app.model.Result
import com.saifkhichi.app.model.User
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject


/**
 * Implements the [WebService] with Firebase Realtime Database.
 *
 * @author saifkhichi96
 * @since 1.0.0
 * @see WebService
 */
class FirebaseDatabaseService @Inject constructor() : WebService {

    private val db = Firebase.database

    override suspend fun getUser(uid: String): Result<User> {
        return try {
            val user = db
                .getReference("users/")
                .child(uid)
                .get()
                .await()
                .getValue(User::class.java) ?: throw IOException("Error logging in")

            Result.Success(user)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    override suspend fun getMessages(): Result<Inbox> {
        return try {
            val messages = Inbox()
            db.getReference("messages/")
                .get()
                .await()
                .children.forEach { snapshot ->
                    snapshot.getValue(Message::class.java)?.let { message ->
                        messages.add(message)
                    }
                }

            Result.Success(messages)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

}