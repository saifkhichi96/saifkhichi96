package com.saifkhichi.app.mail.util

import com.saifkhichi.app.mail.model.EmailConfig
import com.saifkhichi.app.mail.model.Message
import org.simplejavamail.api.email.Email
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.mailer.MailerBuilder


/**
 * A class to send emails.
 */
class MailSender : AsyncTask<Email, Result<Email>>() {

    private val mailer: Mailer = MailerBuilder
        .withSMTPServer(EmailConfig.HOST, EmailConfig.PORT, EmailConfig.REPLY_TO, EmailConfig.PASSWORD)
        .withTransportStrategy(TransportStrategy.SMTP_TLS)
        .buildMailer()

    override fun doInBackground(vararg params: Email): Result<Email> {
        return kotlin.runCatching {
            val email = params[0]
            mailer.sendMail(email)
            email
        }
    }

    /**
     * Sends an auto-generated email to acknowledge the user's message.
     *
     * @param message the message to acknowledge
     */
    fun acknowledgeMessage(message: Message) {
        val acknowledgement = message.generateAcknowledgement()
        send(acknowledgement)
    }

    /**
     * Sends an email.
     *
     * @param email the email to send
     */
    fun send(email: Email) {
        execute(email)
    }

}