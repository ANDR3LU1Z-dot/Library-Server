package br.pucpr.libraryserver.libraries

import br.pucpr.libraryserver.books.Book
import br.pucpr.libraryserver.books.response.BookResponse
import jakarta.persistence.*

@Entity
class Library (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var description: String? = null,

    @OneToMany(mappedBy = "library", cascade = [CascadeType.ALL], orphanRemoval = true)
    var books: MutableSet<Book> = mutableSetOf()
) {
    fun toBook(books: MutableSet<Book>): List<BookResponse> {
        return books.map {
            BookResponse(it)
        }
    }

}