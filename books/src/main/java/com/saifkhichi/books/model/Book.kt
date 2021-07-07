package com.saifkhichi.books.model

import java.io.Serializable

data class Book(
    var id: String = "",
    var isbn: String = "",
    var title: String = "",
    var sortTitle: String = "",
    var authors: String = "",
    var category: String = "",
    var subCategory: String = "",
    var publishedBy: String = "",
    var publishedOn: Int = 0,
    var pageCount: Int = 0,
    var format: String = "",
    var lang: String = "",
    var excerpt: String = "",
) : Serializable {

    fun isbn13(): String {
        return if (isbn.length == 10) {
            var isbn13: String = isbn
            isbn13 = "978" + isbn13.substring(0, 9)
            var d: Int

            var sum = 0
            for (i in isbn13.indices) {
                d = if (i % 2 == 0) 1 else 3
                sum += (isbn13[i].code - 48) * d
            }
            sum = 10 - sum % 10
            isbn13 += sum

            isbn13
        } else {
            isbn
        }
    }

}