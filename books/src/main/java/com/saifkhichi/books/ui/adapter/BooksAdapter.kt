package com.saifkhichi.books.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.saifkhichi.books.R
import com.saifkhichi.books.databinding.ViewBookBinding
import com.saifkhichi.books.databinding.ViewBookListBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.books.ui.holder.BookHolder
import com.saifkhichi.books.ui.holder.BookListHolder

class BooksAdapter(
    private val context: AppCompatActivity,
    private val dataset: ArrayList<LibraryListItem<out Any>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var filteredDataset = ArrayList<LibraryListItem<out Any>>()

    init {
        filteredDataset = dataset
    }

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
                view.setPadding(
                    parent.context.resources.getDimension(R.dimen.activity_horizontal_margin).toInt(),
                    0,
                    0,
                    0
                )
                view.setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline6)
                object : RecyclerView.ViewHolder(view) {}
            }
            TYPE_SUB_CATEGORY -> {
                val view = MaterialTextView(parent.context)
                view.setPadding(
                    parent.context.resources.getDimension(R.dimen.activity_horizontal_margin).toInt(),
                    0,
                    0,
                    0
                )
                view.setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline6)
                object : RecyclerView.ViewHolder(view) {}
            }
            else -> {
                val view = ViewBookListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                BookListHolder(view)
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
        when (val item = filteredDataset[position]) {
            is LibraryBook -> {
                if (holder is BookListHolder) {
                    val books = item.value
                    holder.bookList.removeAllViews()
                    books.forEach { book ->
                        val view = ViewBookBinding.inflate(
                            LayoutInflater.from(holder.bookList.context),
                            holder.bookList,
                            false
                        )
                        holder.bookList.addView(view.root)

                        val bookHolder = BookHolder(view)
                        book.getBookCover(context, bookHolder.bookCover)
                        bookHolder.bookTitle.text = book.title
                        bookHolder.bookAuthors.text = book.authors

                        bookHolder.book = book
                        bookHolder.onItemClicked = onItemClicked
                    }
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
    override fun getItemCount() = filteredDataset.size

    /**
     * Return the type of the item
     */
    override fun getItemViewType(position: Int) = filteredDataset[position].type

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                if (constraint.isNotEmpty()) {
                    val filteredDataset = ArrayList<LibraryListItem<out Any>>()
                    var lastCategory: SubCategoryName? = null
                    for (item in dataset) {
                        if (item is SubCategoryName) lastCategory = item
                        else if (item is LibraryBook) {
                            val books = item.value
                            val filteredBooks = books.filter {
                                it.isbn.equals(constraint.toString(), true) ||
                                        it.isbn13().equals(constraint.toString(), true) ||
                                        it.title.contains(constraint, true) ||
                                        it.authors.contains(constraint, true)
                            }

                            if (filteredBooks.isNotEmpty()) {
                                lastCategory?.let { filteredDataset.add(it) }

                                val filteredItem = LibraryBook(filteredBooks)
                                filteredDataset.add(filteredItem)
                            }
                        }
                    }
                    results.count = filteredDataset.size
                    results.values = filteredDataset
                } else {
                    results.count = dataset.size
                    results.values = dataset
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredDataset = results.values as ArrayList<LibraryListItem<out Any>>
                notifyDataSetChanged()
            }
        }
    }

    sealed class LibraryListItem<T : Any>(val value: T, val type: Int)
    class LibraryBook(books: List<Book>) : LibraryListItem<List<Book>>(books, TYPE_BOOKS)
    class CategoryName(title: String) : LibraryListItem<String>(title, TYPE_CATEGORY)
    class SubCategoryName(title: String) : LibraryListItem<String>(title, TYPE_SUB_CATEGORY)

    companion object {
        private const val TYPE_BOOKS = 0
        private const val TYPE_CATEGORY = 1
        private const val TYPE_SUB_CATEGORY = 2
    }

}