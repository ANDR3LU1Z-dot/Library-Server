package br.pucpr.libraryserver.books

import br.pucpr.libraryserver.books.response.BookResponse
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
    fun findByAuthor(author: String): List<BookResponse>
    fun findByPublicationYear(year: Int): List<BookResponse>
}