package net.letsgg.platform.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import net.letsgg.platform.utility.constants.LetsggConstants.SecurityConstants.AUTH_FILTER_PROCESS_URL
import net.letsgg.platform.utility.constants.LetsggConstants.SecurityConstants.HEADER_STRING
import net.letsgg.platform.utility.constants.LetsggConstants.SecurityConstants.SECRET_KEY
import net.letsgg.platform.utility.constants.LetsggConstants.SecurityConstants.TOKEN_EXPIRE_IN_DAYS
import net.letsgg.platform.utility.constants.LetsggConstants.SecurityConstants.TOKEN_PREFIX
import net.letsgg.platform.webapi.dto.LoginRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.sql.Date
import java.time.LocalDate
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtUserAuthenticationFilter(
    authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {


    init {
        this.authenticationManager = authenticationManager
        setFilterProcessesUrl(AUTH_FILTER_PROCESS_URL)
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication {
        try {
            val authRequest = ObjectMapper().registerKotlinModule()
                .readValue(req.inputStream, LoginRequest::class.java)
            val authentication = UsernamePasswordAuthenticationToken(
                authRequest.login,
                authRequest.password
            )
            return authenticationManager.authenticate(authentication)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication
    ) {
        val token = JWT.create()
            .withSubject(auth.name)
            .withClaim("authorities", auth.authorities.map { it.authority }.toMutableList())
            .withIssuedAt(Date.valueOf(LocalDate.now()))
            .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(TOKEN_EXPIRE_IN_DAYS)))
            .sign(Algorithm.HMAC512(SECRET_KEY))
        res.addHeader(HEADER_STRING, "$TOKEN_PREFIX $token")
    }
}