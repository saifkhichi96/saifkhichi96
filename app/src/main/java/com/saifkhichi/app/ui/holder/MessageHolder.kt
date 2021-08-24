package com.saifkhichi.app.ui.holder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.saifkhichi.app.databinding.ViewMessageBinding
import com.saifkhichi.app.model.Message

class MessageHolder(binding: ViewMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    var message: Message? = null

    val senderIcon: MaterialButton = binding.senderIcon
    val senderName: TextView = binding.sender
    val messageRecipient: TextView = binding.recipient
    val messageTimestamp: TextView = binding.messageTimestamp
    val messageContents: TextView = binding.messageContent

    var onItemClicked: ((message: Message) -> Unit)? = null

    init {
        binding.replyButton.setOnClickListener {
            message?.let { onItemClicked?.invoke(it) }
        }
    }
}