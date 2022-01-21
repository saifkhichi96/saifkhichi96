package com.saifkhichi.app.model

/**
 * Inbox contains all the received [Message]s.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class Inbox : ArrayList<Message>() {

    fun toThreads(): List<Thread> {
        val threads = ArrayList<Thread>()
        this.groupBy { it.from }
            .forEach { (email, list) ->
                list.groupBy { it.subject.lowercase() }.values.forEach { messages ->
                    messages.firstOrNull()?.let {
                        val thread = Thread(it.sender, email, it.subject)
                        thread.addAll(messages)
                        threads.add(thread)
                    }
                }
            }

        return threads.sortedBy { it.latestMessage?.timestamp }
    }

    companion object {
        fun fromList(list: List<Message>): Inbox {
            val inbox = Inbox()
            inbox.addAll(list)
            return inbox
        }
    }

}