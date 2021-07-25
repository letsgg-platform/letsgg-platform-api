package net.letsgg.platform.security.jwt

import net.letsgg.platform.api.dto.OauthTokenInfoDto
import net.letsgg.platform.security.oauth2.OauthTokenInfo
import org.springframework.security.core.Authentication

interface AuthorizationTokenService {
    fun createAuthorizationOauthTokenInfo(authentication: Authentication): OauthTokenInfo
    fun createToken(authentication: Authentication): OauthTokenInfoDto
}
