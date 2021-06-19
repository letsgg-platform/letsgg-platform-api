package net.letsgg.platform.api.dto

import net.letsgg.platform.entity.Dto

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