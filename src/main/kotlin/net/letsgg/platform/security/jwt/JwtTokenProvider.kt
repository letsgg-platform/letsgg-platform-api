package net.letsgg.platform.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import net.letsgg.platform.config.AuthProperties
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.sql.Date
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtTokenProvider(
    private val authProperties: AuthProperties
) {

    internal fun createAccessToken(auth: Authentication): String {
        return JWT.create()
            .withSubject(auth.name)
            .withClaim("authorities", auth.authorities.map { it.authority }.toMutableList())
            .withIssuedAt(Date.valueOf(LocalDate.now()))
            .withExpiresAt(getExpiredDate(authProperties.tokenExpireInMs.toLong()))
            .sign(Algorithm.HMAC512(authProperties.tokenSecretKey))
    }

    internal fun createRefreshToken(auth: Authentication): String {
        return JWT.create()
            .withSubject(auth.name)
            .withClaim("authorities", auth.authorities.map { it.authority }.toMutableList())
            .withIssuedAt(Date.valueOf(LocalDate.now()))
            .withExpiresAt(getExpiredDate(authProperties.refreshTokenExpireInMs.toLong()))
            .sign(Algorithm.HMAC512(authProperties.tokenSecretKey))
    }

    internal fun generateAuthorizationCode(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    internal fun getTokenExpirationInMs(token: String): Long {
        return try {
            val expiresAt = JWT
                .require(Algorithm.HMAC512(authProperties.tokenSecretKey))
                .build()
                .verify(token).expiresAt
            Instant.now().until(expiresAt.toInstant(), ChronoUnit.MILLIS)
        } catch (e: JWTVerificationException) {
            0
        }
    }

    private fun getExpiredDate(tokenExpireInMs: Long): java.util.Date? {
        val expiredDateTime = LocalDateTime.now().plus(tokenExpireInMs, ChronoUnit.MILLIS)
        return Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    internal fun getAccessTokenExpiration(): Long = authProperties.tokenExpireInMs.toLong()

    internal fun getRefreshTokenExpiration(): Long = authProperties.refreshTokenExpireInMs.toLong()
}