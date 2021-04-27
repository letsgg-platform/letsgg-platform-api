package net.letsgg.platform.utility

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.*


object CookieUtils {

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
            path = "/"
            domain = "letsgg.net"
            isHttpOnly = true
            secure = true
            setMaxAge(maxAge)
        }
        response.addCookie(cookie)
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
}