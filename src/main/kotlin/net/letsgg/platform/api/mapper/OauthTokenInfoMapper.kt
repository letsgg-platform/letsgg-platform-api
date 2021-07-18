package net.letsgg.platform.api.mapper

import net.letsgg.platform.api.dto.OauthTokenInfoDto
import net.letsgg.platform.security.oauth2.OauthTokenInfo
import net.letsgg.platform.service.auth.token.JwtTokenUtilService
import org.springframework.stereotype.Component

@Component
class OauthTokenInfoMapper(
  private val jwtTokenUtilService: JwtTokenUtilService
) {

  fun convert(source: OauthTokenInfo): OauthTokenInfoDto {
    val (expiresInMs, refreshTokenExpiresInMs) = getAccessAndRefreshTokenExpirationInMs(
      source.accessToken,
      source.refreshToken
    )
    return OauthTokenInfoDto(
      accessToken = source.accessToken,
      refreshToken = source.refreshToken,
      tokenType = source.tokenType.toString(),
      expiresInMs = expiresInMs,
      refreshTokenExpiresInMs = refreshTokenExpiresInMs
    )
  }

  private fun getAccessAndRefreshTokenExpirationInMs(accessToken: String, refreshToken: String): Pair<Long, Long> {
    with(jwtTokenUtilService) {
      return Pair(getJwtTokenExpirationInMs(accessToken), getJwtTokenExpirationInMs(refreshToken))
    }
  }

}