package com.saifkhichi.app.mail.model

import com.saifkhichi.app.BuildConfig

object EmailConfig {
    const val HOST = "smtp.gmail.com"
    const val PORT = 587

    const val SENDER = BuildConfig.ADMIN_EMAIL
    const val REPLY_TO = BuildConfig.GMAIL_USERNAME
    const val PASSWORD = BuildConfig.GMAIL_PASSWORD

    const val SIGNATURE = "Regards,\nSaif Khan"
}