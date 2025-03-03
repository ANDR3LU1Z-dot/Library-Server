package br.pucpr.libraryserver.books

import br.pucpr.libraryserver.books.response.BookResponse
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional
    fun insert(book: Book): Book {
        return bookRepository.save(book)
    }

    fun getBookById(id: Long): Book {
        return bookRepository.findById(id).orElseThrow { RuntimeException("Book not found") }
    }

    fun findAll() = bookRepository.findAll(Sort.by("title"))

    @Transactional
    fun updateBook(id: Long, updatedBook: Book): Book {
        val existingBook = bookRepository.findById(id).orElseThrow { RuntimeException("Book not found") }
        existingBook.title = updatedBook.title
        existingBook.author = updatedBook.author
        existingBook.publicationYear = updatedBook.publicationYear
        existingBook.isbn = updatedBook.isbn
        return bookRepository.save(existingBook)
    }

    @Transactional
    fun deleteBook(id: Long) {
        bookRepository.deleteById(id)
    }

    fun getBooksByAuthor(author: String): List<BookResponse> {
        return bookRepository.findByAuthor(author)
    }

    fun getBooksByPublicationYear(year: Int): List<BookResponse> {
        return bookRepository.findByPublicationYear(year)
    }
}