package net.letsgg.platform.webapi.dto

import net.letsgg.platform.entity.Dto
import javax.validation.constraints.Email
import javax.validation.constraints.Size

//for sign up process
data class SignUpRequest(
    @Size(min = 3)
    val name: String,
    @Size(min = 3)
    val username: String,
    @Email
    val email: String,
    @Size(min = 8)
    val password: String,
//    val locale: String,
): Dto