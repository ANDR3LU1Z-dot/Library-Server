package br.pucpr.libraryserver.libraries

import org.springframework.data.jpa.repository.JpaRepository

interface LibraryRepository : JpaRepository<Library, Long> {

}
