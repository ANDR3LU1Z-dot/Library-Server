package br.pucpr.libraryserver.users.controller.responses

import br.pucpr.libraryserver.libraries.controller.responses.LibraryResponse
import br.pucpr.libraryserver.users.User

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val library: LibraryResponse?
) {
    constructor(user: User): this(id=user.id!!, user.name, user.email, library = user.toLibrary(user.library))
}
