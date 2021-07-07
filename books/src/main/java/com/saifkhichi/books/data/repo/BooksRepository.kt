package com.saifkhichi.books.data.repo

import com.orhanobut.hawk.Hawk
import com.saifkhichi.books.model.Book
import com.saifkhichi.books.data.source.BooksDataSource
import javax.inject.Inject

/**
 * Requests Inbox data from the remote data source and maintains an in-memory
 * cache of the messages.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class BooksRepository @Inject constructor(var dataSource: BooksDataSource) {

    /**
     * In-memory cache of messages
     */
    var books: List<Book>? = null
        private set

    init {
        kotlin.runCatching { books = Hawk.get(KEY) }
    }

    suspend fun listAll(): Result<List<Book>> {
        val result = dataSource.listAll()
        result.getOrNull()?.let {
            setLibraryData(it)
        }

        return result
    }

    private fun setLibraryData(books: List<Book>) {
        this.books = books
        Hawk.put(KEY, books)
    }

    companion object {
        private const val KEY = "books"
    }

}