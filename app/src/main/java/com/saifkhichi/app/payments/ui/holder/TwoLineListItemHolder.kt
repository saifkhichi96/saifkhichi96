package com.saifkhichi.app.payments.ui.holder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.databinding.ViewListitemTwolineBinding

class TwoLineListItemHolder<T>(binding: ViewListitemTwolineBinding) : RecyclerView.ViewHolder(binding.root) {
    val text1: TextView = binding.text1
    val text2: TextView = binding.text2

    var item: T? = null
    var onItemClicked: ((client: T) -> Unit)? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked?.invoke(it) }
        }
    }
}