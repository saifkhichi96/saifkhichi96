package com.saifkhichi.app.data.repo

import com.orhanobut.hawk.Hawk
import com.saifkhichi.app.data.source.InboxDataSource
import com.saifkhichi.app.model.Inbox
import com.saifkhichi.app.model.Result
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
        kotlin.runCatching { messages = Hawk.get(KEY) }
    }

    suspend fun listAll(): Result<Inbox> {
        val result = dataSource.listAll()
        if (result is Result.Success) {
            setInboxData(result.data)
        }

        return result
    }

    private fun setInboxData(messages: Inbox) {
        this.messages = messages
        Hawk.put(KEY, messages)
    }

    companion object {
        private const val KEY = "messages"
    }

}