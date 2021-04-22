package net.letsgg.platform.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message) {

    companion object {
        fun userIdSupplier(id: UUID): Throwable = throw ResourceNotFoundException("user with id: $id not found")
        fun userEmailSupplier(email: String): Throwable = throw ResourceNotFoundException("user with email: $email not found")
        fun userUsernameSupplier(username: String): Throwable = throw ResourceNotFoundException("user with username $username not found")
    }
}