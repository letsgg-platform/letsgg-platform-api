package net.letsgg.platform.service.oauth

import net.letsgg.platform.repository.OauthTokenRepository
import net.letsgg.platform.security.oauth2.OauthTokenInfo
import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
@Transactional
class CoreOauthTokenService(
  private val oauthTokenRepository: OauthTokenRepository,
) : OauthTokenService {

  @Transactional(readOnly = true)
  override fun getTokenByAuthorizationCode(authorizationCode: String, request: HttpServletRequest): OauthTokenInfo {
    return oauthTokenRepository.findByAuthorizationCode(authorizationCode).orElseThrow {
      OAuth2AuthorizationException(
        OAuth2Error(
          "401",
          "Cannot proceed authentication - Invalid authorization code",
          request.requestURI
        )
      )
    }
  }

  override fun deleteById(id: UUID) {
    oauthTokenRepository.deleteById(id)
  }

  override fun save(oauthTokenInfo: OauthTokenInfo): OauthTokenInfo {
    return oauthTokenRepository.save(oauthTokenInfo)
  }
}