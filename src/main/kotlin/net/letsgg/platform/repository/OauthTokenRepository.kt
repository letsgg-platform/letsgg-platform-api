package net.letsgg.platform.repository

import net.letsgg.platform.security.oauth2.OauthTokenInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OauthTokenRepository : JpaRepository<OauthTokenInfo, UUID> {
  fun findByAuthorizationCode(authorizationCode: String): Optional<OauthTokenInfo>
}