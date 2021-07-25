package net.letsgg.platform.security.oauth2

import net.letsgg.platform.utility.CookieUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class HttpCookieOauth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        private const val cookieExpireSeconds = 180
    }

    override fun loadAuthorizationRequest(req: HttpServletRequest): OAuth2AuthorizationRequest? {
        val cookie: Cookie? = CookieUtils.getCookie(req, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        return cookie?.let {
            CookieUtils.deserialize(it, OAuth2AuthorizationRequest::class.java)
        }
    }

    override fun saveAuthorizationRequest(
        authRequest: OAuth2AuthorizationRequest?,
        req: HttpServletRequest,
        response: HttpServletResponse
    ) {
        if (authRequest == null) {
            removeAuthorizationRequestCookies(req, response)
            return
        }

        CookieUtils.addCookie(
            response,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(authRequest),
            cookieExpireSeconds
        )
        val redirectUriAfterLogin = req.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (redirectUriAfterLogin.isNotBlank()) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(req: HttpServletRequest): OAuth2AuthorizationRequest? {
        return this.loadAuthorizationRequest(req)
    }

    fun removeAuthorizationRequestCookies(req: HttpServletRequest, response: HttpServletResponse) {
        with(CookieUtils) {
            deleteCookie(req, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            deleteCookie(req, response, REDIRECT_URI_PARAM_COOKIE_NAME)
        }
    }
}
