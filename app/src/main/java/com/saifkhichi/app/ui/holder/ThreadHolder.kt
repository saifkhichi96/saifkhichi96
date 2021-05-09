package com.saifkhichi.app.ui.holder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.databinding.ViewListitemTwolineBinding
import com.saifkhichi.app.model.Thread

class ThreadHolder(binding: ViewListitemTwolineBinding) : RecyclerView.ViewHolder(binding.root) {
    var thread: Thread? = null

    val senderName: TextView = binding.text1
    val messageTimestamp: TextView = binding.text2

    var onItemClicked: ((thread: Thread) -> Unit)? = null

    init {
        binding.root.setOnClickListener {
            thread?.let { onItemClicked?.invoke(it) }
        }
    }
}