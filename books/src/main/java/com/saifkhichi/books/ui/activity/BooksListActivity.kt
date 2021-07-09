package com.saifkhichi.books.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.saifkhichi.books.R
import com.saifkhichi.books.databinding.ActivityBooksListBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.books.ui.activity.BookDetailsActivity.Companion.EXTRA_BOOK
import com.saifkhichi.books.ui.adapter.BooksAdapter
import com.saifkhichi.books.ui.viewmodel.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksListActivity : AppCompatActivity() {

    private lateinit var booksAdapter: BooksAdapter
    private val library = ArrayList<BooksAdapter.LibraryListItem<out Any>>()

    private var categoryFilter: String? = null
    private var subCategoryFilter: String? = null

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

        categoryFilter = intent.getStringExtra(EXTRA_CATEGORY)
        subCategoryFilter = intent.getStringExtra(EXTRA_CATEGORY)
        supportActionBar?.title = subCategoryFilter ?: categoryFilter ?: getString(R.string.title_activity_library)

        booksAdapter = BooksAdapter(this, library)
        booksAdapter.setOnItemClickListener { openBookDetails(it) }

        binding.booksList.adapter = booksAdapter
        viewModel.books.observe(this, refreshResultObserver)

        binding.swipeRefresh.setOnRefreshListener(onRefreshListener)
        refreshManually()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_books_list, menu)

        // Associate searchable configuration with the SearchView
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            this.queryHint = getString(R.string.hint_search)
            this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return onSearchRequested(query.trim())
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return if (newText.isEmpty()) onSearchRequested("") else false
                }
            })
        }
        return true
    }

    private fun onSearchRequested(query: String): Boolean {
        booksAdapter.filter.filter(query)
        return true
    }

    private fun openBookDetails(book: Book) {
        val i = Intent(this, BookDetailsActivity::class.java)
        i.putExtra(EXTRA_BOOK, book)

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
            if (categoryFilter == null || category == categoryFilter) {
                if (category != categoryFilter) library += BooksAdapter.CategoryName(category)

                val subCategories = group.value.groupBy { it.subCategory }.toSortedMap()
                subCategories.forEach { item ->
                    val subCategory = item.key.ifBlank { getString(R.string.book_category_none) }
                    library += BooksAdapter.SubCategoryName(
                        if (category == categoryFilter) subCategory
                        else getString(R.string.book_category).format(category, subCategory)
                    )

                    val subCategoryBooks = item.value.sortedBy { it.sortTitle }
                    library += BooksAdapter.LibraryBook(subCategoryBooks)
                }
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

    companion object {
        const val EXTRA_CATEGORY = "category"
        const val EXTRA_SUBCATEGORY = "sub_category"
    }

}