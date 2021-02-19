package com.saifkhichi.app.ui.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.model.Thread

class ThreadHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thread: Thread? = null

    val senderName: TextView = view.findViewById(android.R.id.text1)
    val messageTimestamp: TextView = view.findViewById(android.R.id.text2)

    var onItemClicked: ((thread: Thread) -> Unit)? = null

    init {
        view.setOnClickListener {
            thread?.let { onItemClicked?.invoke(it) }
        }
    }
}