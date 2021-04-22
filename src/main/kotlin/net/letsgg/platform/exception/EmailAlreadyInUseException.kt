package net.letsgg.platform.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
class EmailAlreadyInUseException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

    companion object {

        fun alreadySubscribedToNewsFeedSupplier(userEmail: String): Throwable {
            throw EmailAlreadyInUseException("$userEmail has already been subscribed")
        }

        fun emailAlreadyInUseSupplier(userEmail: String): Throwable {
            throw EmailAlreadyInUseException("$userEmail already in use")
        }
    }


}