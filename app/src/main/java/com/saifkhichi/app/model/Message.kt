package com.saifkhichi.app.model

import java.io.Serializable

/**
 * Data class that captures details of messages retrieved from the
 * MessageRepository.
 *
 * @property email The email address of the sender.
 * @property message The contents of the message.
 * @property name The name of the sender.
 * @property subject The subject of the message.
 * @property timestamp The timestamp when the message was sent.
 *
 * @author saifkhichi96
 */
data class Message(
    var email: String = "",
    var message: String = "",
    var name: String = "",
    var subject: String = "",
    var timestamp: Long = 0L,
) : Serializable