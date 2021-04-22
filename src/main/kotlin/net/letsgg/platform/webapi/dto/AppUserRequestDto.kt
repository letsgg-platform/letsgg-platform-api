package net.letsgg.platform.webapi.dto

import net.letsgg.platform.entity.Dto
import java.time.LocalDate

/*
* dto for updating user fields
* */
class AppUserRequestDto(
    val fullName: String,
    val userName: String,
    val email: String,
    val password: String,
    val country: String,
): Dto