package com.saifkhichi.books.ui.holder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.books.databinding.ViewBookBinding
import com.saifkhichi.books.model.Book

class BookHolder(binding: ViewBookBinding) : RecyclerView.ViewHolder(binding.root) {
    var book: Book? = null

    val bookTitle: TextView = binding.title
    val bookAuthors: TextView = binding.authors

    var onItemClicked: ((thread: Book) -> Unit)? = null

    init {
        binding.root.setOnClickListener {
            book?.let { onItemClicked?.invoke(it) }
        }
    }
}