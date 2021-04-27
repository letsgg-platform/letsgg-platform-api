package net.letsgg.platform.security.jwt

import net.letsgg.platform.security.TokenType
import net.letsgg.platform.security.oauth2.OauthTokenInfo
import net.letsgg.platform.service.user.AppUserService
import net.letsgg.platform.webapi.dto.OauthTokenInfoModel
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtTokenProviderProxy(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: AppUserService
) {

    fun createOauthTokenInfo(authentication: Authentication): OauthTokenInfo {
        val user = userService.getByEmail(authentication.name)
        with(jwtTokenProvider) {
            val accessToken = createAccessToken(authentication)
            val refreshToken = createRefreshToken(authentication)
            val authorizationCode = generateAuthorizationCode()
            return OauthTokenInfo(authorizationCode, accessToken, refreshToken, TokenType.BEARER, user)
        }
    }

    fun createToken(authentication: Authentication): OauthTokenInfoModel {
        with(jwtTokenProvider) {
            return OauthTokenInfoModel(
                createAccessToken(authentication),
                createRefreshToken(authentication),
                TokenType.BEARER.value,
                getAccessTokenExpiration(),
                getRefreshTokenExpiration()
            )
        }
    }
}