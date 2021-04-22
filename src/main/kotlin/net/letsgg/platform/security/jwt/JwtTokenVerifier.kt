package net.letsgg.platform.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import net.letsgg.platform.config.AuthProperties
import net.letsgg.platform.utility.CookieUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenVerifier(
    private val authProperties: AuthProperties
) : OncePerRequestFilter() {

    /**
     * Processes the header of each request for the access token.
     * <p>
     *  If the token is present and is valid -> user's authentication object is being
     *     injected into {@code SecurityContextHolder}.
     *  otherwise -> the authentication object is not injected, which results
     *      into {@see Unauthorized HttpStatus code}.
     * </p>
     * */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

//        val accessTokenCookie = CookieUtils.getCookie(request, "accessToken")
//        val tokenTypeCookie = CookieUtils.getCookie(request, "tokenType")

        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

//        if (tokenTypeCookie == null || accessTokenCookie == null || tokenTypeCookie.value != "Bearer" || accessTokenCookie.value.isEmpty()) {
//            filterChain.doFilter(request, response)
//            return
//        }

        try {
            val token = authHeader.replace("Bearer ", "")
            val decodedJWT = JWT
                .require(Algorithm.HMAC512(authProperties.tokenSecretKey))
                .build()
                .verify(token)
            val userEmail = decodedJWT.subject
            val authorities = decodedJWT.getClaim("authorities").asArray(String::class.java)
            val grantedAuthorities = authorities
                .map { SimpleGrantedAuthority(it) }
            val authentication = UsernamePasswordAuthenticationToken(
                userEmail,
                null,
                grantedAuthorities
            )
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: JWTVerificationException) {
            throw RuntimeException("Invalid auth token", e)
        }
        filterChain.doFilter(request, response)
    }
}