package com.saifkhichi.app.mail.data.repo

import com.orhanobut.hawk.Hawk
import com.saifkhichi.app.core.model.Result
import com.saifkhichi.app.mail.data.source.InboxDataSource
import com.saifkhichi.app.mail.model.Inbox
import com.saifkhichi.app.mail.model.Message
import javax.inject.Inject

/**
 * Requests Inbox data from the remote data source and maintains an in-memory
 * cache of the messages.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class InboxRepository @Inject constructor(var dataSource: InboxDataSource) {

    /**
     * In-memory cache of messages
     */
    var messages: Inbox? = null
        private set

    init {
        kotlin.runCatching { messages = Inbox.fromList(Hawk.get(KEY)) }
    }

    suspend fun listAll(): Result<Inbox> {
        val result = dataSource.listAll()
        if (result is Result.Success) {
            setInboxData(result.data)
        }

        return result
    }

    suspend fun deleteMessage(message: Message) {
        // Delete from remote data source
        dataSource.delete(message)

        // Delete from local cache
        messages?.let {
            it.remove(message)
            setInboxData(it)
        }
    }

    suspend fun markAsRead(messageId: String) {
        // Mark as read in local cache
        messages?.find { it.id == messageId }?.read = true
        messages?.let { setInboxData(it) }

        // Mark as read in remote data source
        messages?.find { it.id == messageId }?.let { dataSource.markAsRead(it) }
    }

    private fun setInboxData(messages: Inbox) {
        this.messages = messages
        Hawk.put(KEY, messages)
    }

    companion object {
        private const val KEY = "messages"
    }

}