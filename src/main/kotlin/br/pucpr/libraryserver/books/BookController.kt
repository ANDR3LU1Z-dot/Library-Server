package br.pucpr.libraryserver.books

import br.pucpr.libraryserver.books.controller.CreateBookRequest
import br.pucpr.libraryserver.books.response.BookResponse
import br.pucpr.libraryserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(val bookService: BookService) {

    @PostMapping
    @SecurityRequirement(name = "AuthServer")
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
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun delete(@PathVariable id: Long, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        return if (user.id == id || user.isAdmin) {
            bookService.deleteBook(id).let { ResponseEntity.ok().build() }
        }
        else
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
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