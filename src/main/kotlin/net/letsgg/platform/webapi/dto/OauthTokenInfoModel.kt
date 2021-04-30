package net.letsgg.platform.webapi.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class OauthTokenInfoModel(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    @JsonProperty("expiresIn")
    val expiresInMs: Long,
    @JsonIgnore
    val refreshTokenExpiresInMs: Long
)