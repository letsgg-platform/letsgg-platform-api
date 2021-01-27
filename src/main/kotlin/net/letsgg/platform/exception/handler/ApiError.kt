package net.letsgg.platform.exception.handler

import org.springframework.http.HttpStatus
import java.time.Instant

class ApiError(
    val error: HttpStatus,
    val message: String,
    val path: String
) {
    val status = error.value()
    val timeStamp = Instant.now()

}