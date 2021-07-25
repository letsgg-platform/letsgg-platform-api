package net.letsgg.platform.exception.handler

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.springframework.http.HttpStatus
import java.time.Instant

@JsonPropertyOrder(value = ["code", "status", "message", "path", "timestamp"])
@JsonInclude(NON_EMPTY)
data class ApiError(
    @get:JsonProperty("status")
    val httpStatus: HttpStatus,
    val message: String?,
    val path: String,
    val timestamp: Instant = Instant.now()
) {
    @get:JsonProperty("code")
    val statusCode: Int = httpStatus.value()

    var errors: Any? = null
}
