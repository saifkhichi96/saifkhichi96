package com.saifkhichi.app.model

import com.saifkhichi.app.mail.model.EmailConfig
import org.simplejavamail.api.email.Email
import org.simplejavamail.email.EmailBuilder
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data class that captures details of messages retrieved from the
 * MessageRepository.
 *
 * @property id The ID of the message
 * @property to The email address of the receiver
 * @property from The email address of the sender
 * @property body The body of the message
 * @property sender The name of the sender
 * @property subject The subject of the message
 * @property replyTo The reply-to address of the message
 * @property timestamp The timestamp when the message was sent.
 * @property read Whether the message has been read or not.
 *
 * @author saifkhichi96
 */
data class Message(
    var id: String = "",
    var to: String = "",
    var from: String = "",
    var body: String = "",
    var sender: String = "",
    var subject: String = "",
    var replyTo: String = "",
    var timestamp: Long = 0L,
    var read: Boolean = false,
) : Serializable {

    private fun quote(): String {
        val senderName = this.sender
        val senderEmail = this.from

        val dateFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.format(this.timestamp)
        return "\n\n\n" +
                "On $date, $senderName <$senderEmail> wrote:\n\n" +
                "> ${this.body.split("\n").joinToString("\n> ")}"
    }

    fun generateAcknowledgement(): Email {
        val response = Message()
        response.timestamp = System.currentTimeMillis()
        response.to = this.from
        response.from = EmailConfig.SENDER
        response.replyTo = EmailConfig.REPLY_TO
        response.subject = "RE: Your message to Saif Khan - ${this.subject}"
        response.body = "Dear ${this.sender},\n\n" +
                "Thank you for reaching out to me. I have received your message and I will " +
                "get back to you as soon as possible.\n\n${EmailConfig.SIGNATURE}"

        return EmailBuilder.startingBlank()
            .to(response.to)
            .from(response.from)
            .withSubject(response.subject)
            .withPlainText(response.body + this.quote())
            .withReplyTo(response.replyTo)
            .buildEmail()
    }

}