package br.pucpr.libraryserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LibraryserverApplication

fun main(args: Array<String>) {
	runApplication<LibraryserverApplication>(*args)
}
