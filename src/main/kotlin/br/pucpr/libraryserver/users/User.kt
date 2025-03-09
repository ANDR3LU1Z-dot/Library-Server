package br.pucpr.libraryserver.users

import br.pucpr.libraryserver.books.Book
import br.pucpr.libraryserver.books.response.BookResponse
import br.pucpr.libraryserver.libraries.Library
import br.pucpr.libraryserver.libraries.controller.responses.LibraryResponse
import br.pucpr.libraryserver.roles.Role
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "tblUsers")
class User (
    @Id @GeneratedValue
    var id: Long? = null,

    @NotBlank
    var name: String,

    @NotBlank
    var password: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @ManyToMany
    @JoinTable(
        name="UserRoles",
        joinColumns = [JoinColumn(name = "idUser")],
        inverseJoinColumns = [JoinColumn(name = "idRole")]
    )
    val roles: MutableSet<Role> = mutableSetOf(),

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "library_id", nullable = true)
    var library: Library? = null

) {
    constructor() : this(null, "", "", "")


    fun toLibrary(library: Library?): LibraryResponse? {
        return library?.let { LibraryResponse(it) }
    }
}