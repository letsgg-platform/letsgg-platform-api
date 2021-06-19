package net.letsgg.platform.security

import net.letsgg.platform.utility.LoggerDelegate
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val logger by LoggerDelegate()

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AuthenticationException
    ) {
        logger.error("Responding with unauthorized error. Message - ${e.message}")
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.localizedMessage)
    }
}