package br.pucpr.libraryserver.libraries.controller.responses

import br.pucpr.libraryserver.books.Book
import br.pucpr.libraryserver.books.response.BookResponse
import br.pucpr.libraryserver.libraries.Library

data class LibraryResponse(
    val id: Long,
    val name: String,
    val description: String,
    val books: List<BookResponse>
) {

    constructor(library: Library): this(id=library.id!!,name = library.name, description= library.description!!, books = library.toBook(library.books))
}