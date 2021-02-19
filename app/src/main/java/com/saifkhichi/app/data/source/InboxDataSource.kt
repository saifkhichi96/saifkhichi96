package com.saifkhichi.app.data.source

import com.saifkhichi.app.db.remote.WebService
import com.saifkhichi.app.model.Inbox
import com.saifkhichi.app.model.Result
import javax.inject.Inject

/**
 * Class that handles retrieves and manages the Inbox data from the web service.
 *
 * @author saifkhichi96
 */
class InboxDataSource @Inject constructor(var webService: WebService) {

    /**
     * Get a list of all the messages from the web service.
     */
    @Throws(Exception::class)
    suspend fun listAll(): Result<Inbox> {
        return webService.getMessages()
    }

}