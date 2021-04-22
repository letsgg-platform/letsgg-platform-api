package net.letsgg.platform.webapi.dto

import net.letsgg.platform.entity.Dto

data class AppUserResponseDto(
    val email: String,
    val hasFinishedSetup: Boolean,
    val isEmailVerified: Boolean
): Dto