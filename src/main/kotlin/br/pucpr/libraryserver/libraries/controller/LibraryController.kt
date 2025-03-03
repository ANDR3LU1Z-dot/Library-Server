package br.pucpr.libraryserver.libraries.controller

import br.pucpr.libraryserver.libraries.LibraryService
import br.pucpr.libraryserver.libraries.SortDir
import br.pucpr.libraryserver.libraries.controller.requests.CreateLibraryRequest
import br.pucpr.libraryserver.libraries.controller.responses.LibraryResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/libraries")
class LibraryController(
    val libraryService: LibraryService
) {

    @PostMapping
    fun insert(@RequestBody @Valid library: CreateLibraryRequest) =
        libraryService.insert(library.toLibrary())
            .let { LibraryResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun findAll(@RequestParam dir: String = "ASC"): ResponseEntity<List<LibraryResponse>> {
        val sortDir = SortDir.findOrNull(dir)
        if(sortDir == null)
            return ResponseEntity.badRequest().build()
        return libraryService.findAll(sortDir)
            .map { LibraryResponse(it) }
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        libraryService.findByIdOrNull(id)
            ?.let { LibraryResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        libraryService.delete(id)
            .let { ResponseEntity.ok().build() }

    @PostMapping("/{libraryId}/books/{bookId}")
    fun addBookToLibrary(@PathVariable libraryId: Long, @PathVariable bookId: Long) {
        libraryService.addBookToLibrary(libraryId, bookId)
    }

    @DeleteMapping("/{libraryId}/books/{bookId}")
    fun removeBookFromLibrary(@PathVariable libraryId: Long, @PathVariable bookId: Long) {
        libraryService.removeBookFromLibrary(libraryId, bookId)
    }
}