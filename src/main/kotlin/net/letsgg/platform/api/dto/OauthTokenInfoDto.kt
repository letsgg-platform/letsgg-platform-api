package net.letsgg.platform.api.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class OauthTokenInfoDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    @JsonProperty("expiresIn")
    val expiresInMs: Long,
    @JsonIgnore
    val refreshTokenExpiresInMs: Long
)
