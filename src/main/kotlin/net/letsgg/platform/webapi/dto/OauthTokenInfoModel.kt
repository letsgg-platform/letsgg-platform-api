package net.letsgg.platform.webapi.dto

data class OauthTokenInfoModel(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshTokenExpiresIn: Long
)