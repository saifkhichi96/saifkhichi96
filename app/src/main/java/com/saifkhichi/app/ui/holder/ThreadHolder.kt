package com.saifkhichi.app.ui.holder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.saifkhichi.app.databinding.ViewThreadBinding
import com.saifkhichi.app.model.Thread

class ThreadHolder(binding: ViewThreadBinding) : RecyclerView.ViewHolder(binding.root) {
    var thread: Thread? = null

    val senderIcon: MaterialButton = binding.senderIcon
    val senderName: TextView = binding.sender
    val messageCount: TextView = binding.count
    val messageTimestamp: TextView = binding.time
    val messageSubject: TextView = binding.subject
    val messageContents: TextView = binding.message

    var onItemClicked: ((thread: Thread) -> Unit)? = null

    init {
        binding.root.setOnClickListener {
            thread?.let { onItemClicked?.invoke(it) }
        }
    }
}