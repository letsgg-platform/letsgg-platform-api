package net.letsgg.platform.service.oauth

import net.letsgg.platform.security.oauth2.OauthTokenInfo
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.servlet.http.HttpServletRequest

interface OauthTokenService {
  fun getTokenByAuthorizationCode(authorizationCode: String, request: HttpServletRequest): OauthTokenInfo
  fun deleteById(id: UUID)
  fun save(oauthTokenInfo: OauthTokenInfo): OauthTokenInfo
}