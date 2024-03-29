package com.saifkhichi.app.mail.ui.holder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.saifkhichi.app.databinding.ViewMessageBinding
import com.saifkhichi.app.mail.model.Message

class MessageHolder(binding: ViewMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    var message: Message? = null

    val senderIcon: MaterialButton = binding.senderIcon
    val senderName: TextView = binding.sender
    val messageRecipient: TextView = binding.recipient
    val messageTimestamp: TextView = binding.messageTimestamp
    val messageContents: TextView = binding.messageContent
    val replyButton: MaterialButton = binding.replyButton
    val deleteButton: MaterialButton = binding.deleteButton

    var onItemClicked: ((message: Message) -> Unit)? = null

    var onItemDeleteClicked: ((message: Message) -> Unit)? = null

    init {
        binding.replyButton.setOnClickListener {
            message?.let { onItemClicked?.invoke(it) }
        }

        binding.deleteButton.setOnClickListener {
            message?.let { onItemDeleteClicked?.invoke(it) }
        }
    }
}