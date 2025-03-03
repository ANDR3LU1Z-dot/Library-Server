package br.pucpr.libraryserver.libraries

import br.pucpr.libraryserver.books.BookRepository
import br.pucpr.libraryserver.expection.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LibraryService(
    val libraryRepository: LibraryRepository,
    val bookRepository: BookRepository
) {
    fun insert(library: Library): Library = libraryRepository.save(library)

    fun findAll(dir: SortDir): List<Library> {
        return when (dir) {
            SortDir.ASC -> libraryRepository.findAll(Sort.by("name"))
            SortDir.DESC -> libraryRepository.findAll(Sort.by("name").descending())
        }

    }

    fun findByIdOrNull(id: Long) = libraryRepository.findByIdOrNull(id)

    fun delete(id: Long) = libraryRepository.deleteById(id)

    @Transactional
    fun addBookToLibrary(libraryId: Long, bookId: Long) {
        val library = libraryRepository.findById(libraryId).orElseThrow { NotFoundException("Library $libraryId not found") }
        val book = bookRepository.findById(bookId).orElseThrow { NotFoundException("Book $bookId not found") }
        book.library = library
        library.books.add(book)
        libraryRepository.save(library)
    }

    @Transactional
    fun removeBookFromLibrary(libraryId: Long, bookId: Long) {
        val library = libraryRepository.findById(libraryId).orElseThrow {  NotFoundException("Library $libraryId not found") }
        val book = bookRepository.findById(bookId).orElseThrow { NotFoundException("Book $bookId not found") }
        book.library = null
        library.books.remove(book)
        libraryRepository.save(library)
    }

}