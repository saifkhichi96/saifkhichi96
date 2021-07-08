package com.saifkhichi.books.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.saifkhichi.books.R
import com.saifkhichi.books.databinding.ActivityBookDetailsBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.storage.CloudFileStorage
import kotlinx.coroutines.launch


class BookDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailsBinding

    private lateinit var bookStorage: CloudFileStorage
    private lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookStorage = CloudFileStorage(this, "library")
        book = intent.getSerializableExtra(BOOK_KEY) as Book? ?: return finish()
        updateUI()

        binding.bookCover.setOnClickListener { pickImage() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            try {
                when (requestCode) {
                    PICK_COVER_REQUEST -> data.data?.apply { updateBookCover(this) }
                }
            } catch (ignored: Exception) {

            }
        }
    }

    private fun updateBookCover(coverImage: Uri) {
        lifecycleScope.launch {
            try {
                val error = bookStorage.upload(book.cover(), coverImage).exceptionOrNull()
                if (error != null) throw error

                book.getBookCover(this@BookDetailsActivity, binding.bookCover, true)
            } catch (ex: Exception) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.book_cover_error),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateUI() {
        book.getBookCover(this@BookDetailsActivity, binding.bookCover)
        binding.bookTitle.text = book.title
        binding.bookAuthors.text = book.authors

        binding.bookCategory.text = getString(R.string.book_category).format(
            book.category, book.subCategory.ifBlank { getString(R.string.book_category_none) }
        )

        binding.bookPages.text = book.pageCount.takeIf { it > 0 }?.toString() ?: getString(R.string.value_none)
        binding.bookFormat.text = book.format.ifBlank { getString(R.string.value_none) }
        binding.bookLanguage.text = book.lang.ifBlank { getString(R.string.value_none) }

        binding.bookPublisher.text = getString(R.string.book_publisher).format(
            book.publishedBy.ifBlank { getString(R.string.book_publisher_none) },
            if (book.publishedOn > 0) getString(R.string.book_publish_year).format(book.publishedOn) else ""
        )

        binding.bookIsbn.text = getString(R.string.book_isbn).format(
            book.isbn.ifBlank { getString(R.string.value_none) }
        )

        binding.bookDescription.text = book.excerpt.ifBlank { getString(R.string.book_description_none) }

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(book.isbn13(), BarcodeFormat.EAN_13, 512, 256)
            binding.bookBarcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, getString(R.string.book_cover_select)), PICK_COVER_REQUEST)
    }

    companion object {
        const val BOOK_KEY = "book"
        const val PICK_COVER_REQUEST = 100
    }

}