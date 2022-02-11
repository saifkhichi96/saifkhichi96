package com.saifkhichi.app.db.remote

import com.saifkhichi.app.model.Inbox
import com.saifkhichi.app.model.Message
import com.saifkhichi.app.model.Result
import com.saifkhichi.app.model.User

/**
 * Defines an interface for two-way communication with a remote database.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
interface WebService {

    /**
     * Retrieves details of a User from the remote database.
     *
     * @param uid The unique id of the User to fetch.
     * @return The Result of the task containing User data, or an error message.
     */
    suspend fun getUser(uid: String): Result<User>

    /**
     * Retrieves a list of all Messages in the remote database.
     *
     * @return The Result of the task containing Message list, or an error message.
     */
    suspend fun getMessages(): Result<Inbox>

    /**
     * Update the read status of a Message in the remote database.
     */
    suspend fun readMessage(message: Message)

}