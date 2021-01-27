package net.letsgg.platform.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
class EmailAlreadyInUseException(message: String) : RuntimeException(message) {

    companion object {

        fun alreadySubscribedToNewsFeedSupplier(userEmail: String): Throwable {
            throw EmailAlreadyInUseException("$userEmail has already been subscribed")
        }
    }
}