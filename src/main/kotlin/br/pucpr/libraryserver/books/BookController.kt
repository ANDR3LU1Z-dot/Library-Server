package br.pucpr.libraryserver.books

import br.pucpr.libraryserver.books.controller.CreateBookRequest
import br.pucpr.libraryserver.books.response.BookResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(val bookService: BookService) {

    @PostMapping
    fun insert(@RequestBody @Valid book: CreateBookRequest) =
        bookService.insert(book.toBook())
            .let { BookResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<BookResponse>> {
        return bookService.findAll()
            .map { BookResponse(it) }
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long) =
        bookService.getBookById(id)
            .let { BookResponse(it) }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody updatedBook: Book): ResponseEntity<BookResponse> {
        return bookService.updateBook(id, updatedBook)
            .let { BookResponse(it) }
            .let { ResponseEntity.ok(it) }
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<Void> {
        bookService.deleteBook(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/author/{author}")
    fun getBooksByAuthor(@PathVariable author: String): ResponseEntity<List<BookResponse>> {
        val books = bookService.getBooksByAuthor(author)
        return ResponseEntity(books, HttpStatus.OK)
    }

    @GetMapping("/year/{year}")
    fun getBooksByPublicationYear(@PathVariable year: Int): ResponseEntity<List<BookResponse>> {
        val books = bookService.getBooksByPublicationYear(year)
        return ResponseEntity(books, HttpStatus.OK)
    }
}