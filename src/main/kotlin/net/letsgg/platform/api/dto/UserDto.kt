package net.letsgg.platform.api.dto

import com.fasterxml.jackson.annotation.JsonView
import net.letsgg.platform.api.view.Views
import net.letsgg.platform.api.view.Views.*
import net.letsgg.platform.entity.Dto
import net.letsgg.platform.entity.type.Gender
import java.time.Instant
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@JsonView(value = [Request::class, Response::class, Update::class])
data class UserDto(
  @get:Size(min = 3)
  val fullName: String?,
  @get:Size(min = 3)
  @field:JsonView(value = [Request::class, Response::class])
  val userName: String,
  @get:Email
  @field:JsonView(value = [Request::class, Response::class])
  val email: String,
) : Dto {
  @field:JsonView(Request::class)
  @get:Size(min = 8, message = "{javax.validation.constraints.Password.message}")
  var password: String? = null

  var genderType: Gender? = null

  var imageUrl: String? = null

  var birthdayDate: LocalDate? = null

  @field:JsonView(Response::class)
  var hasFinishedSetup: Boolean? = null

  @field:JsonView(Response::class)
  var isEmailVerified: Boolean? = null
}