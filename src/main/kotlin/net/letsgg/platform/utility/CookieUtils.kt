package net.letsgg.platform.utility

import net.letsgg.platform.api.dto.OauthTokenInfoDto
import org.springframework.http.ResponseCookie
import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


object CookieUtils {

    private const val DEFAULT_COOKIE_DOMAIN = "letsgg.net"
    private const val DEFAULT_COOKIE_PATH = "/"
    private const val DEFAULT_COOKIE_MAX_AGE = 999L
    private const val ACCESS_TOKEN_COOKIE = "usaccessjwt"
    private const val REFRESH_TOKEN_COOKIE = "usrefreshjwt"
    private const val TOKEN_TYPE_COOKIE = "token_type"

    fun getCookie(req: HttpServletRequest, name: String): Cookie? {
        val cookies: Array<Cookie>? = req.cookies
        cookies?.forEach { cookie ->
            if (cookie.name == name) {
                return cookie
            }
        }
        return null
    }

    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.apply {
            path = DEFAULT_COOKIE_PATH
            domain = DEFAULT_COOKIE_DOMAIN
            isHttpOnly = true
            secure = true
            setMaxAge(maxAge)
        }
        response.addCookie(cookie)
    }

    fun newCookie(
        name: String,
        value: String,
        maxAge: Long = DEFAULT_COOKIE_MAX_AGE,
        path: String = DEFAULT_COOKIE_PATH,
        isHttpOnly: Boolean = true,
        isSecure: Boolean = true
    ): ResponseCookie {
        return ResponseCookie.from(name, value)
            .domain(DEFAULT_COOKIE_DOMAIN)
            .path(path)
            .maxAge(maxAge)
            .httpOnly(isHttpOnly)
            .secure(isSecure)
            .build()
    }

    fun deleteCookie(req: HttpServletRequest, response: HttpServletResponse, name: String) {
        val cookies = req.cookies
        cookies?.forEach { cookie ->
            if (cookie.name == name) {
                cookie.apply {
                    value = ""
                    path = "/"
                    maxAge = 0
                }
                response.addCookie(cookie)
            }
        }
    }

    fun serialize(`object`: Any): String {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(`object`))
    }

    fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
        return cls.cast(
            SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.value)
            )
        )
    }

    fun setAuthCookies(
      oauthTokenInfo: OauthTokenInfoDto,
      response: HttpServletResponse
    ) {
        addCookie(
            response,
            ACCESS_TOKEN_COOKIE,
            oauthTokenInfo.accessToken,
            oauthTokenInfo.expiresInMs.toInt() / 1000
        )
        addCookie(
            response,
            REFRESH_TOKEN_COOKIE,
            oauthTokenInfo.refreshToken,
            oauthTokenInfo.refreshTokenExpiresInMs.toInt() / 1000
        )
        addCookie(
            response,
            TOKEN_TYPE_COOKIE,
            oauthTokenInfo.tokenType,
            oauthTokenInfo.refreshTokenExpiresInMs.toInt() / 1000
        )
    }
}