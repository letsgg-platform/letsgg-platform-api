package net.letsgg.platform.webapi.dto

import net.letsgg.platform.entity.Dto
import java.time.LocalDate

data class LetsggUserResponseDto(
    val userName: String,
    val birthdayDate: LocalDate,
    val country: String,
): Dto
