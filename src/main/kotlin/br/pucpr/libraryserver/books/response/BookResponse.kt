package br.pucpr.libraryserver.books.response

import br.pucpr.libraryserver.books.Book

data class BookResponse(
    val id: Long,
    val title: String,
    val author: String,
    val publicationYear: Int,
    val isbn: String
) {
    constructor(book: Book): this(id=book.id!!,title = book.title, author = book.author, publicationYear = book.publicationYear, isbn = book.isbn)
}
