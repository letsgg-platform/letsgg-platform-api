package net.letsgg.platform.service.auth

import net.letsgg.platform.api.dto.LoginRequest
import net.letsgg.platform.api.dto.OauthTokenInfoDto
import net.letsgg.platform.api.dto.UserDto

interface UserAuthService {
    fun login(loginRequest: LoginRequest): OauthTokenInfoDto
    fun register(userDto: UserDto): OauthTokenInfoDto
}
