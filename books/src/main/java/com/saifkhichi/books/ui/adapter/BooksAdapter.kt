package com.saifkhichi.books.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.saifkhichi.books.R
import com.saifkhichi.books.databinding.ViewBookBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.books.ui.holder.BookHolder

class BooksAdapter(private val dataset: ArrayList<LibraryListItem<out Any>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClicked: ((Book) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Book) -> Unit)) {
        onItemClicked = listener
    }

    /**
     * Create new views, which defines the UI of the list item
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> {
                val view = MaterialTextView(parent.context)
                view.setTextAppearance(R.style.TextAppearance_MaterialComponents_Subtitle1)
                object : RecyclerView.ViewHolder(view) {}
            }
            TYPE_SUB_CATEGORY -> {
                val view = MaterialTextView(parent.context)
                view.setTextAppearance(R.style.TextAppearance_MaterialComponents_Subtitle2)
                object : RecyclerView.ViewHolder(view) {}
            }
            else -> {
                val view = ViewBookBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                BookHolder(view)
            }
        }
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * Get element from the dataset at this [position] and replace the
     * contents of the view with that element
     *
     * @param holder The view to update with new content
     * @param position The position of new content in the dataset
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = dataset[position]) {
            is LibraryBook -> {
                if (holder is BookHolder) {
                    val book = item.value
                    holder.bookTitle.text = book.title
                    holder.bookAuthors.text = book.authors

                    holder.book = book
                    holder.onItemClicked = onItemClicked
                }
            }
            is CategoryName -> {
                val textView = holder.itemView
                if (textView is MaterialTextView) {
                    val category = item.value
                    textView.text = category
                }
            }
            is SubCategoryName -> {
                val textView = holder.itemView
                if (textView is MaterialTextView) {
                    val subCategory = item.value
                    textView.text = subCategory
                }
            }
        }
    }

    /**
     * Return the size of the item
     */
    override fun getItemCount() = dataset.size

    /**
     * Return the type of the item
     */
    override fun getItemViewType(position: Int) = dataset[position].type

    sealed class LibraryListItem<T : Any>(val value: T, val type: Int)
    class LibraryBook(book: Book) : LibraryListItem<Book>(book, TYPE_BOOK)
    class CategoryName(title: String) : LibraryListItem<String>(title, TYPE_CATEGORY)
    class SubCategoryName(title: String) : LibraryListItem<String>(title, TYPE_SUB_CATEGORY)

    companion object {
        private const val TYPE_BOOK = 0
        private const val TYPE_CATEGORY = 1
        private const val TYPE_SUB_CATEGORY = 2
    }

}