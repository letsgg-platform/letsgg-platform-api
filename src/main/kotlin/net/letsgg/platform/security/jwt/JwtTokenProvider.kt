package net.letsgg.platform.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.security.core.Authentication
import java.sql.Date
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

object JwtTokenProvider {

    internal fun createAccessToken(auth: Authentication, tokenExpireInMs: Long, tokenSecretKey: String): String {
        return JWT.create()
            .withSubject(auth.name)
            .withClaim("authorities", auth.authorities.map { it.authority }.toMutableList())
            .withIssuedAt(Date.valueOf(LocalDate.now()))
            .withExpiresAt(getExpiredDate(tokenExpireInMs))
            .sign(Algorithm.HMAC512(tokenSecretKey))
    }

    internal fun createRefreshToken(auth: Authentication, refreshTokenExpireInMs: Long, tokenSecretKey: String): String {
        return JWT.create()
            .withSubject(auth.name)
            .withClaim("authorities", auth.authorities.map { it.authority }.toMutableList())
            .withIssuedAt(Date.valueOf(LocalDate.now()))
            .withExpiresAt(getExpiredDate(refreshTokenExpireInMs))
            .sign(Algorithm.HMAC512(tokenSecretKey))
    }

    internal fun generateAuthorizationCode(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    internal fun getTokenExpirationInMs(token: String, tokenSecretKey: String): Long {
        return try {
            val expiresAt = JWT
                .require(Algorithm.HMAC512(tokenSecretKey))
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
}
