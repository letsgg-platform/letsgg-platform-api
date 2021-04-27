package net.letsgg.platform.service

import net.letsgg.platform.exception.InvalidResetPasswordTokenException
import net.letsgg.platform.repository.OauthTokenRepository
import net.letsgg.platform.repository.PasswordResetTokenRepository
import net.letsgg.platform.security.jwt.JwtTokenProvider
import net.letsgg.platform.security.oauth2.OauthTokenInfo
import net.letsgg.platform.service.oauth.OauthTokenService
import net.letsgg.platform.webapi.dto.OauthTokenInfoModel
import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import javax.servlet.http.HttpServletRequest

@Service
@Transactional
class AppTokenService(
    private val resetTokenRepository: PasswordResetTokenRepository,
    private val oauthTokenService: OauthTokenService,
) {

    fun validateResetPasswordToken(token: String) {
        val persistedToken =
            resetTokenRepository.findByToken(token).orElseThrow { throw InvalidResetPasswordTokenException() }

        if (persistedToken.expireDate.isBefore(Instant.now())) {
            throw InvalidResetPasswordTokenException("token expired")
        }
    }

    fun saveOauthToken(oauthTokenInfo: OauthTokenInfo): OauthTokenInfo = oauthTokenService.save(oauthTokenInfo)

    fun getOauthTokenByAuthorizationCode(authorizationCode: String, request: HttpServletRequest): OauthTokenInfoModel {
        val oauthTokenInfo = oauthTokenService.getTokenByAuthorizationCode(authorizationCode, request)
        oauthTokenService.deleteById(oauthTokenInfo.id!!)
        return OauthTokenInfoModel(
            oauthTokenInfo.accessToken,
            oauthTokenInfo.refreshToken,
            oauthTokenInfo.tokenType.value,
            oauthTokenService.getOauthTokenExpirationInMs(oauthTokenInfo.accessToken),
            oauthTokenService.getOauthTokenExpirationInMs(oauthTokenInfo.refreshToken)
        )
    }
}