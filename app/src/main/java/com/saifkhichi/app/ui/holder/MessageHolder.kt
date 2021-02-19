package com.saifkhichi.app.ui.holder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.databinding.ViewMessageBinding
import com.saifkhichi.app.model.Message

class MessageHolder(binding: ViewMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    var message: Message? = null

    val messageContent: TextView = binding.messageContent
    val messageSubject: TextView = binding.messageSubject
    val messageTimestamp: TextView = binding.messageTimestamp

    var onItemClicked: ((message: Message) -> Unit)? = null

    init {
        binding.replyButton.setOnClickListener {
            message?.let { onItemClicked?.invoke(it) }
        }
    }
}