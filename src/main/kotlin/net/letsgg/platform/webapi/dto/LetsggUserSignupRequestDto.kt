package net.letsgg.platform.webapi.dto

import net.letsgg.platform.entity.Dto
import java.time.LocalDate

//for sign up process
data class LetsggUserSignupRequestDto(
    val firstName: String,
    val lastName: String,
    val userName: String,
    val email: String,
    val password: String,
    val country: String,
    val birthdate: LocalDate,
    val locale: String,
): Dto
