package com.saifkhichi.books.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.saifkhichi.books.R
import com.saifkhichi.books.databinding.ActivityBooksListBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.books.ui.activity.BookDetailsActivity.Companion.BOOK_KEY
import com.saifkhichi.books.ui.adapter.BooksAdapter
import com.saifkhichi.books.ui.viewmodel.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksListActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BooksAdapter
    private val library = ArrayList<BooksAdapter.LibraryListItem<out Any>>()

    /**
     * View bindings for the activity.
     */
    private lateinit var binding: ActivityBooksListBinding

    /**
     * View model to observe and manipulate the activity data.
     */
    private val viewModel: BooksViewModel by viewModels()

    /**
     * Performs the refresh operation.
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getAllBooks()
    }

    /**
     * Observes the result of the refresh operation.
     */
    private val refreshResultObserver = Observer<Result<List<Book>>> {
        binding.swipeRefresh.isRefreshing = false
        try {
            val data = it.getOrThrow()
            onRefreshed(data)
        } catch (ex: Exception) {
            onRefreshFailed(ex.message ?: ex::class.java.simpleName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBooksListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        booksAdapter = BooksAdapter(library)
        booksAdapter.setOnItemClickListener { openBookDetails(it) }

        binding.booksList.adapter = booksAdapter
        (binding.booksList.layoutManager as GridLayoutManager?)?.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (library[position]) {
                        is BooksAdapter.LibraryBook -> 1
                        is BooksAdapter.CategoryName -> 3
                        is BooksAdapter.SubCategoryName -> 3
                    }
                }
            }
        viewModel.books.observe(this, refreshResultObserver)

        binding.swipeRefresh.setOnRefreshListener(onRefreshListener)
        refreshManually()
    }

    private fun openBookDetails(book: Book) {
        val i = Intent(this, BookDetailsActivity::class.java)
        i.putExtra(BOOK_KEY, book)

        startActivity(i)
    }

    /**
     * Refreshes the activity content manually (i.e. not with the swipe-refresh gesture)
     */
    private fun refreshManually() {
        binding.swipeRefresh.isRefreshing = true
        onRefreshListener.onRefresh()
    }

    /**
     * Called when refresh operation finishes successfully
     *
     * @param data The updated data.
     */
    private fun onRefreshed(data: List<Book>) {
        library.clear()
        val categories = data.groupBy { it.category }.toSortedMap()
        categories.forEach { group ->
            val category = group.key
            library += BooksAdapter.CategoryName(category)

            val subCategories = group.value.groupBy { it.subCategory }.toSortedMap()
            subCategories.forEach { item ->
                var subCategory = item.key
                if (subCategory.isBlank()) subCategory = getString(R.string.book_category_none)
                library += BooksAdapter.SubCategoryName(getString(R.string.book_category).format(
                    category, subCategory.ifBlank { getString(R.string.book_category_none) }
                ))

                val subCategoryBooks = item.value.sortedBy { it.sortTitle }
                subCategoryBooks.forEach { library += BooksAdapter.LibraryBook(it) }
            }
        }
        booksAdapter.notifyDataSetChanged()
    }

    /**
     * Called when the refresh operation fails.
     *
     * @param error An error message explaining the cause of failure.
     */
    private fun onRefreshFailed(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

}