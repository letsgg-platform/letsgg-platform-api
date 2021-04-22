package net.letsgg.platform.exception.handler.oauth2

import net.letsgg.platform.security.oauth2.HttpCookieOauth2AuthorizationRequestRepository
import net.letsgg.platform.security.oauth2.HttpCookieOauth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

import net.letsgg.platform.utility.CookieUtils.getCookie
import org.springframework.security.core.AuthenticationException

import javax.servlet.ServletException

import java.io.IOException

import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest


@Component
class OAuth2AuthenticationFailureHandler(
    private val httpCookieOauth2AuthorizationRequestRepository: HttpCookieOauth2AuthorizationRequestRepository,
) : SimpleUrlAuthenticationFailureHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val targetUrl: String = getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.name ?: "/"
        targetUrl.let {
            UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.localizedMessage)
                .build().toUriString()
        }
        httpCookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}