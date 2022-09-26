package com.saifkhichi.books.data.source


import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.services.books.v1.Books
import com.google.api.services.books.v1.model.Volume


/**
 * Interface for accessing the Google Books API.
 */
object GoogleBooksAPI {

    val api = Books.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        AndroidJsonFactory.getDefaultInstance(),
        null
    ).setApplicationName("Books").build()

    /**
     * Query the API for a list of books matching the given ISBN.
     */
    fun findByISBN(isbn: String): List<Volume> {
        return api.volumes().list("isbn:$isbn").execute().items?.toList().orEmpty()
    }

}