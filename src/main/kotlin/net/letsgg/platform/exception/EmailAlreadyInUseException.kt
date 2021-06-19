package net.letsgg.platform.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

class EmailAlreadyInUseException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

}