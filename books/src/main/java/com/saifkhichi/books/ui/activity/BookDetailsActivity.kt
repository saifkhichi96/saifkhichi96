package com.saifkhichi.books.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.saifkhichi.books.R
import com.saifkhichi.books.databinding.ActivityBookDetailsBinding
import com.saifkhichi.books.model.Book
import com.saifkhichi.storage.CloudFileStorage


class BookDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val book = intent.getSerializableExtra(BOOK_KEY) as Book? ?: return finish()
        updateUI(book)
    }

    private fun updateUI(book: Book) {
        val storage = CloudFileStorage(this, "library")
        storage.download(book.id + ".jpg") { result ->
            Glide.with(this@BookDetailsActivity)
                .load(result.getOrNull())
                .thumbnail()
                .placeholder(R.drawable.placeholder_book_cover)
                .error(R.drawable.placeholder_book_cover)
                .into(binding.bookCover)
        }

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

    companion object {
        const val BOOK_KEY = "book"
    }

}