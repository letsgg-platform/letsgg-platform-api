package net.letsgg.platform.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import net.letsgg.platform.config.AuthProperties
import net.letsgg.platform.utility.CookieUtils
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenVerifier(
    private val authProperties: AuthProperties,
    private val jwtTokenProviderProxy: JwtTokenProviderProxy
) : OncePerRequestFilter() {

    /**
     * Determines the platform of the request.
     * And then filters the request based on the platform of the client
     * In case of mobile -> validates 'Authorization' header from the request
     * In case of web -> validates all the authorization cookies
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
        if (request.getHeader("Device-Type") == "android") {
            filterMobileRequest(request, response, filterChain)
        } else if (request.getHeader("Device-Type") == "web") {
            filterWebRequest(request, response, filterChain)
        }
        filterChain.doFilter(request, response)
    }

    private fun filterMobileRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = getMobileAccessToken(request)
        setSecurityContext(accessToken)
        filterChain.doFilter(request, response)
    }

    private fun getMobileAccessToken(request: HttpServletRequest): String {
        val authHeader = request.getHeader("Authorization")
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            throw RuntimeException("invalid authorization header for mobile client")
        }
        return authHeader.replace("Bearer ", "")
    }

    /**
     * Validates web request.
     * Validates accessToken cookie, if not valid then the refreshCookie is being validated
     * If refreshCookie is valid -> refreshes authorization cookies
     * If not -> throws Exception
     * */
    private fun filterWebRequest(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        var shouldRefreshToken = false
        val accessTokenCookie = CookieUtils.getCookie(request, "usaccessjwt")
        val refreshTokenCookie = CookieUtils.getCookie(request, "usrefreshjwt")
        val tokenTypeCookie = CookieUtils.getCookie(request, "token_type")

        if (tokenTypeCookie == null || accessTokenCookie == null || tokenTypeCookie.value != "Bearer" || accessTokenCookie.value.isEmpty()) {
            filterChain.doFilter(request, response)
            return
        }
        var token = accessTokenCookie.value
        if (jwtTokenProviderProxy.isTokenExpired(token)) {
            if (refreshTokenCookie == null) {
                filterChain.doFilter(request, response)
                return
            }
            shouldRefreshToken = true
            token = refreshTokenCookie.value
        }
        setSecurityContext(token)
        //refresh token here?
        if (shouldRefreshToken) {
            refreshToken(response)
        }
        filterChain.doFilter(request, response)
    }

    private fun refreshToken(response: HttpServletResponse) {
        val oauthTokenInfo = jwtTokenProviderProxy.createToken(SecurityContextHolder.getContext().authentication)
        with(CookieUtils) {
            addCookie(
                response,
                "usaccessjwt",
                oauthTokenInfo.accessToken,
                oauthTokenInfo.expiresInMs.toInt() / 1000
            )
            addCookie(
                response,
                "usrefreshjwt",
                oauthTokenInfo.refreshToken,
                oauthTokenInfo.refreshTokenExpiresInMs.toInt() / 1000
            )
            addCookie(
                response,
                "token_type",
                oauthTokenInfo.tokenType,
                oauthTokenInfo.refreshTokenExpiresInMs.toInt() / 1000
            )
        }
    }

    private fun setSecurityContext(token: String) {
        try {
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
    }
}