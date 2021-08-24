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
        this.groupBy { it.email }
            .forEach { (email, list) ->
                list.groupBy { it.subject.lowercase() }.values.forEach { messages ->
                    messages.firstOrNull()?.let {
                        val thread = Thread(it.name, email, it.subject)
                        thread.addAll(messages)
                        threads.add(thread)
                    }
                }
            }

        return threads
    }

}