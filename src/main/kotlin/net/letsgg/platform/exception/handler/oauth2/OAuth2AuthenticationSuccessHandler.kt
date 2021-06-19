package net.letsgg.platform.exception.handler.oauth2

import net.letsgg.platform.config.AuthProperties
import net.letsgg.platform.exception.OAuth2UnauthorizedRedirectUriException
import net.letsgg.platform.security.jwt.AuthorizationTokenService
import net.letsgg.platform.security.oauth2.HttpCookieOauth2AuthorizationRequestRepository
import net.letsgg.platform.security.oauth2.HttpCookieOauth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import net.letsgg.platform.service.oauth.OauthTokenService
import net.letsgg.platform.utility.CookieUtils.getCookie
import net.letsgg.platform.utility.OAUTH2_REDIRECT_ALREADY_COMMITTED
import net.letsgg.platform.utility.OAUTH2_REDIRECT_URI_EXCEPTION
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
@Transactional
class OAuth2AuthenticationSuccessHandler(
  private val httpCookieOauth2AuthorizationRequestRepository: HttpCookieOauth2AuthorizationRequestRepository,
  private val authorizationTokenService: AuthorizationTokenService,
  private val oauthTokenService: OauthTokenService,
  private val authProperties: AuthProperties
) : SimpleUrlAuthenticationSuccessHandler() {

  companion object {
    private const val CODE_QUERY_PARAM = "code"
  }

  @Throws(IOException::class, ServletException::class)
  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ) {
    val targetUrl = determineTargetUrl(request, response, authentication)
    if (response.isCommitted) {
      logger.error(String.format(OAUTH2_REDIRECT_ALREADY_COMMITTED, targetUrl))
      return
    }
    clearAuthenticationAttributes(request, response)
    redirectStrategy.sendRedirect(request, response, targetUrl)
  }

  /**
   * Determines target uri for sending authorization code.
   * */
  override fun determineTargetUrl(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ): String {
    val redirectUri: String? = getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.value
    if ((redirectUri != null && redirectUri.isNotBlank()) && !isAuthorizedRedirectUri(redirectUri)) {
      logger.error(String.format(OAUTH2_REDIRECT_URI_EXCEPTION, redirectUri))
      throw OAuth2UnauthorizedRedirectUriException(String.format(OAUTH2_REDIRECT_URI_EXCEPTION, redirectUri))
    }
    val targetUrl: String = redirectUri ?: defaultTargetUrl
    val oauthTokenInfo =
      oauthTokenService.save(authorizationTokenService.createAuthorizationOauthTokenInfo(authentication))
    return UriComponentsBuilder.fromUriString(targetUrl)
      .queryParam(CODE_QUERY_PARAM, oauthTokenInfo.authorizationCode)
      .build().toUriString()
  }

  protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
    super.clearAuthenticationAttributes(request)
    httpCookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
  }

  private fun isAuthorizedRedirectUri(uri: String?): Boolean {
    if (uri == null) {
      return false
    }
    val clientRedirectUri: URI = URI.create(uri)
    return authProperties.authorizedRedirectUris
      .any { authorizedRedirectUri ->
        // Only validate host and port. Let the clients use different paths if they want to
        val authorizedURI: URI = URI.create(authorizedRedirectUri)
        if (authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true)
          && authorizedURI.port == clientRedirectUri.port
        ) {
          return@any true
        }
        false
      }
  }
}