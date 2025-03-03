package br.pucpr.libraryserver.books

import br.pucpr.libraryserver.libraries.Library
import jakarta.persistence.*

@Entity
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var author: String,

    @Column(nullable = false)
    var publicationYear: Int,

    @Column(nullable = false, unique = true)
    var isbn: String,

    @ManyToOne
    @JoinColumn(name = "library_id", nullable = true)
    var library: Library? = null
)