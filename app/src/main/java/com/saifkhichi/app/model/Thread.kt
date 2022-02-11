package com.saifkhichi.app.model

/**
 * Thread is a list of [Message]s from the same sender.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
data class Thread constructor(
    var senderName: String = "",
    var senderEmail: String = "",
    var subject: String = "",
) : ArrayList<Message>() {

    val latestMessage: Message?
        get() = kotlin.runCatching { this[this.size - 1] }.getOrNull()

    companion object {
        fun fromList(list: List<Message>): Thread? {
            return list.firstOrNull()?.let {
                Thread(it.sender, it.from, it.subject).apply { addAll(list) }
            }
        }
    }

}