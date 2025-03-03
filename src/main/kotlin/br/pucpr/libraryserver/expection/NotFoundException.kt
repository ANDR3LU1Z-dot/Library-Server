package br.pucpr.libraryserver.expection

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(
    message: String? = "Not Found",
    cause: Throwable? = null
): IllegalArgumentException(message, cause) {

}