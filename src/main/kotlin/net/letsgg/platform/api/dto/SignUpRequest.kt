package net.letsgg.platform.api.dto

import net.letsgg.platform.entity.Dto
import javax.validation.constraints.Email
import javax.validation.constraints.Size

//for sign up process
data class SignUpRequest(
  @get:Size(min = 3)
  val name: String,
  @get:Size(min = 3)
  val username: String,
  @get:Email
  val email: String,
  @get:Size(min = 8, message = "{javax.validation.constraints.Password.message}")
  val password: String,
//    val locale: String,
) : Dto