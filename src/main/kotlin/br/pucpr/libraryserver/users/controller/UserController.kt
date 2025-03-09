package br.pucpr.libraryserver.users.controller

import br.pucpr.libraryserver.libraries.LibraryService
import br.pucpr.libraryserver.security.UserToken
import br.pucpr.libraryserver.users.SortDir
import br.pucpr.libraryserver.users.UserService
import br.pucpr.libraryserver.users.controller.requests.CreateUserRequest
import br.pucpr.libraryserver.users.controller.requests.LoginRequest
import br.pucpr.libraryserver.users.controller.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

//Classe que vai gerar rotas para o servidor
@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val libraryService: LibraryService
) {

    @PostMapping()
    fun insert(@RequestBody @Valid user: CreateUserRequest) =
        userService.insert(user.toUser())
            .let { UserResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }


    @GetMapping
    fun findAll(@RequestParam dir: String = "ASC", @RequestParam role: String? = null): ResponseEntity<List<UserResponse>> {
        val sortDir = SortDir.findOrNull(dir)
        if(sortDir == null)
            return ResponseEntity.badRequest().build()
        return userService.findAll(sortDir, role)
            .map { UserResponse(it) }
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        userService.findByIdOrNull(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun delete(@PathVariable id: String, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        val uid = if(id == "me") user.id else id.toLong()
        return if (user.id == uid || user.isAdmin) {
             userService.delete(uid).let { ResponseEntity.ok().build() }
        }
        else
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @PutMapping("/{id}/roles/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AuthServer")
    fun grant(@PathVariable id: Long, @PathVariable role: String): ResponseEntity<Void> =
        if (userService.addRole(id, role)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()

    @PostMapping("/login")
    fun login(@Valid @RequestBody user: LoginRequest) =
        userService.login(user.email!!, user.password!!)
            ?.let { ResponseEntity.ok().body(it) }
            ?:ResponseEntity.notFound().build()

    @PutMapping("/{id}/libraries/{libraryId}")
    @SecurityRequirement(name = "AuthServer")
    fun addLibraryToUser(@PathVariable id: Long, @PathVariable libraryId: Long): ResponseEntity<Void> =
        if(userService.addLibrary(id, libraryId)) ResponseEntity.ok().build()
        else ResponseEntity.badRequest().build()

    @DeleteMapping("/{id}/libraries/{libraryId}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun removeLibraryFromUser(@PathVariable id: String, @PathVariable libraryId: Long, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        val uid = if(id == "me") user.id else id.toLong()
        return if (user.id == uid || user.isAdmin) {
            if (userService.removeLibrary(uid ,libraryId)) {
                ResponseEntity.ok().build()
            } else {
                ResponseEntity.notFound().build()
            }
        }
        else
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

}