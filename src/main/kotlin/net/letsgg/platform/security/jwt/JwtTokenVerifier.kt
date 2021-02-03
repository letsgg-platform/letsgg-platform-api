package net.letsgg.platform.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import net.letsgg.platform.utility.constants.LetsggConstants.SecurityConstants.SECRET_KEY
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.RuntimeException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenVerifier: OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        try {
            val token = authHeader.replace("Bearer ", "")
            val decodedJWT = JWT
                .require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token)
            val username = decodedJWT.subject
            val authorities = decodedJWT.getClaim("authorities").asArray(String::class.java)
            val simpleGrantedAuthorities = authorities
                .map { SimpleGrantedAuthority(it) }
            val authentication = UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
            )
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: JWTVerificationException){
            throw RuntimeException("Invalid auth token", e)
        }
        filterChain.doFilter(request, response)
    }
}