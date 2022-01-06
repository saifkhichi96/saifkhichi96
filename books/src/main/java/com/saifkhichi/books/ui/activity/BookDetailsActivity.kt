package com.saifkhichi.books.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.zxing.BarcodeFormat
import com.saifkhichi.books.R
import com.saifkhichi.books.data.source.BooksDataSource
import com.saifkhichi.books.databinding.ActivityBookDetailsBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.books.util.BarcodeEncoder
import com.saifkhichi.storage.CloudFileStorage
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailsBinding

    private lateinit var bookStorage: CloudFileStorage
    private lateinit var book: Book

    @Inject
    lateinit var repo: BooksDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        findViewById<View>(android.R.id.content).transitionName = "open_book_details"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 250L
        }
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close)

        bookStorage = CloudFileStorage(this, "library")
        book = intent.getSerializableExtra(EXTRA_BOOK) as Book? ?: return finish()
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_EDIT_BOOK -> if (resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    book = repo.get(book.id) ?: book
                    updateUI()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                editBook()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun editBook() {
        val i = Intent(this, EditBookActivity::class.java)
        i.putExtra(EXTRA_BOOK, book)

        startActivityForResult(i, RC_EDIT_BOOK)
    }

    private fun updateUI() {
        book.getBookCover(this, binding.bookCover)
        binding.bookTitle.text = book.title
        binding.bookAuthors.text = book.authors

        binding.bookCategory.text = getString(R.string.book_category).format(
            book.category, book.subCategory.ifBlank { getString(R.string.book_category_none) }
        )

        binding.bookPages.text = book.pageCount.takeIf { it > 0 }?.toString() ?: getString(R.string.value_none)
        binding.bookFormat.text = book.format.toString().ifBlank { getString(R.string.value_none) }
        binding.bookLanguage.text = book.lang.ifBlank { getString(R.string.value_none) }

        binding.bookPublisher.text = getString(R.string.book_publisher).format(
            book.publishedBy,
            if (book.publishedOn > 0) getString(R.string.book_publish_year).format(book.publishedOn) else ""
        )

        binding.bookIsbn.text = book.isbn
        binding.bookDescription.text = book.excerpt.ifBlank { getString(R.string.book_description_none) }

        try {
            val barcodeEncoder = BarcodeEncoder()

            val qrColor = TypedValue()
            theme.resolveAttribute(R.attr.colorOnSurface, qrColor, true)
            barcodeEncoder.setForegroundColor(qrColor.data)
            barcodeEncoder.setBackgroundColor(Color.TRANSPARENT)
            val bitmap = barcodeEncoder.encodeBitmap(book.isbn13(), BarcodeFormat.EAN_13, 512, 128)
            binding.bookBarcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val EXTRA_BOOK = "book"
        const val RC_EDIT_BOOK = 100
    }

}