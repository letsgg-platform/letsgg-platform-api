package net.letsgg.platform.service.auth.token

interface JwtTokenUtilService {
    fun getJwtTokenExpirationInMs(token: String): Long
    fun isTokenExpired(token: String): Boolean
}
