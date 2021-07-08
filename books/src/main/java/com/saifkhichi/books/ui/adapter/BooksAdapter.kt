package com.saifkhichi.books.ui.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.saifkhichi.books.R
import com.saifkhichi.books.databinding.ViewBookBinding
import com.saifkhichi.books.databinding.ViewBookListBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.books.ui.holder.BookHolder
import com.saifkhichi.books.ui.holder.BookListHolder
import com.saifkhichi.storage.CloudFileStorage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class BooksAdapter(private val context: AppCompatActivity, private val dataset: ArrayList<LibraryListItem<out Any>>) :
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
        when (val item = dataset[position]) {
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
    override fun getItemCount() = dataset.size

    /**
     * Return the type of the item
     */
    override fun getItemViewType(position: Int) = dataset[position].type

    private fun showBookCover(book: Book, imageView: ImageView, invalidate: Boolean = false) {
        val bookStorage = CloudFileStorage(imageView.context, "library")
        bookStorage.download(book.cover(), invalidate) { result ->
            try {
                Glide.with(imageView)
                    .load(result.getOrNull()!!)
                    .thumbnail()
                    .placeholder(R.drawable.placeholder_book_cover)
                    .error(R.drawable.placeholder_book_cover)
                    .into(imageView)
            } catch (ex: Exception) {
                if (book.isbn13().isNotBlank()) {
                    val queue = Volley.newRequestQueue(imageView.context)
                    val url = "https://www.googleapis.com/books/v1/volumes?q=isbn:${book.isbn13()}"
                    val jsonObjectRequest = JsonObjectRequest(
                        Request.Method.GET, url, null,
                        { response ->
                            kotlin.runCatching {
                                val coverUrl = response
                                    .getJSONArray("items")
                                    .getJSONObject(0)
                                    .getJSONObject("volumeInfo")
                                    .getJSONObject("imageLinks")
                                    .getString("thumbnail")
                                    .replace("http://", "https://")

                                val imageRequest = ImageRequest(
                                    coverUrl,
                                    { response: Bitmap ->
                                        val stream = ByteArrayOutputStream()
                                        response.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                                        val byteArray: ByteArray = stream.toByteArray()
                                        response.recycle()
                                        context.lifecycleScope.launch {
                                            val error = bookStorage.upload(book.cover(), byteArray).exceptionOrNull()
                                            if (error == null) showBookCover(book, imageView, invalidate = true)
                                        }
                                    },
                                    128,
                                    256,
                                    ImageView.ScaleType.CENTER_CROP,
                                    Bitmap.Config.ARGB_8888,
                                    {

                                    }
                                )

                                queue.add(imageRequest)
                            }
                        }, {})
                    queue.add(jsonObjectRequest)
                }
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