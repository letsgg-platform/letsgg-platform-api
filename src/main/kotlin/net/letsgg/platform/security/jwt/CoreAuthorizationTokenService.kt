package net.letsgg.platform.security.jwt

import net.letsgg.platform.api.dto.OauthTokenInfoDto
import net.letsgg.platform.config.AuthProperties
import net.letsgg.platform.security.TokenType
import net.letsgg.platform.security.oauth2.OauthTokenInfo
import net.letsgg.platform.service.auth.token.JwtTokenUtilService
import net.letsgg.platform.service.user.UserService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CoreAuthorizationTokenService(
  private val userService: UserService,
  private val authProperties: AuthProperties
) : AuthorizationTokenService, JwtTokenUtilService {

  /**
   * Creates {@code authorization_code} and associated {@code access_token/refresh_token}.
   * {@code authorization_code} is then used to exchange it for access_token/refresh_token pair.
   * */
  override fun createAuthorizationOauthTokenInfo(authentication: Authentication): OauthTokenInfo {
    val tokenExpireInMs = authProperties.tokenExpireInMs.toLong()
    val refreshTokenExpireInMs = authProperties.refreshTokenExpireInMs.toLong()
    val user = userService.getByEmail(authentication.name)
    with(JwtTokenProvider) {
      val accessToken = createAccessToken(authentication, tokenExpireInMs, authProperties.tokenSecretKey)
      val refreshToken = createRefreshToken(authentication, refreshTokenExpireInMs, authProperties.tokenSecretKey)
      val authorizationCode = generateAuthorizationCode()
      return OauthTokenInfo(authorizationCode, accessToken, refreshToken, TokenType.BEARER, user)
    }
  }

  override fun createToken(authentication: Authentication): OauthTokenInfoDto {
    val tokenExpireInMs = authProperties.tokenExpireInMs.toLong()
    val refreshTokenExpireInMs = authProperties.refreshTokenExpireInMs.toLong()
    with(JwtTokenProvider) {
      return OauthTokenInfoDto(
        createAccessToken(authentication, tokenExpireInMs, authProperties.tokenSecretKey),
        createRefreshToken(authentication, refreshTokenExpireInMs, authProperties.tokenSecretKey),
        TokenType.BEARER.value,
        tokenExpireInMs,
        refreshTokenExpireInMs
      )
    }
  }

  override fun isTokenExpired(token: String): Boolean {
    return JwtTokenProvider.getTokenExpirationInMs(token, authProperties.tokenSecretKey) <= 0
  }

  override fun getJwtTokenExpirationInMs(token: String): Long {
    return JwtTokenProvider.getTokenExpirationInMs(token, authProperties.tokenSecretKey)
  }
}