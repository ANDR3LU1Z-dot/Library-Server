package br.pucpr.libraryserver.libraries.controller.requests

import br.pucpr.libraryserver.books.Book
import br.pucpr.libraryserver.libraries.Library
import jakarta.validation.constraints.NotBlank

data class CreateLibraryRequest (

    @field:NotBlank
    val name: String?,

    val description: String?,

) {
    fun toLibrary() = Library (
        name = name!!,
        description = description!!,
    )
}