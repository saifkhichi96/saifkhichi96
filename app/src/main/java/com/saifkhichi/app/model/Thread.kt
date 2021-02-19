package com.saifkhichi.app.model

/**
 * Thread is a list of [Message]s from the same sender.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
data class Thread(
    var senderEmail: String = "",
) : ArrayList<Message>() {

    companion object {
        fun fromList(list: List<Message>): Thread? {
            return list.firstOrNull()?.email?.let {
                Thread(it).apply { addAll(list) }
            }
        }
    }

}