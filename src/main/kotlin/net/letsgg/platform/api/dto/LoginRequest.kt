package net.letsgg.platform.api.dto

import net.letsgg.platform.entity.Dto

data class LoginRequest(
    val email: String,
    val password: String,
) : Dto
