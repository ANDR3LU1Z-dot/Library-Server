package br.pucpr.libraryserver.books.controller

import br.pucpr.libraryserver.books.Book
import jakarta.validation.constraints.NotNull

data class CreateBookRequest(

    @field:NotNull
    val title: String,

    @field:NotNull
    val author: String,

    @field:NotNull
    val publicationYear: Int,

    @field:NotNull
    val isbn: String
) {
    fun toBook() = Book(
        title = title,
        author = author,
        publicationYear = publicationYear,
        isbn = isbn
    )
}
