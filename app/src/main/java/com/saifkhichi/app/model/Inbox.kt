package com.saifkhichi.app.model

/**
 * Inbox contains all the received [Message]s.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class Inbox : ArrayList<Message>() {

    fun toThreads(): List<Thread> {
        return this.groupBy { it.email }.map { map ->
            Thread(map.key).apply { addAll(map.value) }
        }
    }

}