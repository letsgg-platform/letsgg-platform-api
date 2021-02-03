package net.letsgg.platform.webapi.dto

import net.letsgg.platform.entity.Dto
import java.time.LocalDate

/*
* dto for updating user fields
* */
class LetsggUserRequestDto(
    val firstName: String,
    val lastName: String,
    val userName: String,
    val email: String,
    val password: String,
    val country: String,
): Dto
