package net.letsgg.platform.api.dto

import com.fasterxml.jackson.annotation.JsonView
import net.letsgg.platform.api.view.Views.Request
import net.letsgg.platform.api.view.Views.Response
import net.letsgg.platform.entity.Dto
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@JsonView(value = [Request::class, Response::class])
data class UserDto(
    @get:Size(min = 3)
    val fullName: String?,
    @get:Size(min = 3)
    val userName: String,
    @get:Email
    val email: String,
) : Dto {
    @field:JsonView(Request::class)
    @get:Size(min = 8, message = "{javax.validation.constraints.Password.message}")
    var password: String? = null

    @field:JsonView(Response::class)
    var hasFinishedSetup: Boolean? = null

    @field:JsonView(Response::class)
    var isEmailVerified: Boolean? = null
}