package com.saifkhichi.app.mail.data.source

import com.saifkhichi.app.core.model.Result
import com.saifkhichi.app.mail.model.Inbox
import com.saifkhichi.app.mail.model.Message
import io.github.saifkhichi96.android.db.RemoteDatabase
import javax.inject.Inject

/**
 * Class that handles retrieves and manages the Inbox data from the web service.
 *
 * @author saifkhichi96
 */
class InboxDataSource @Inject constructor(var db: RemoteDatabase) {

    /**
     * Get a list of all the messages from the web service.
     */
    @Throws(Exception::class)
    suspend fun listAll(): Result<Inbox> {
        return try {
            val messages = db.list("messages/", Message::class.java, null, null)
            val inbox = Inbox.fromList(messages)
            Result.Success(inbox)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    suspend fun markAsRead(message: Message) {
        db.update("messages/${message.id}/read", true)
    }

    suspend fun delete(message: Message) {
        db.remove("messages/${message.id}")
    }

}