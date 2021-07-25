package net.letsgg.platform.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "auth")
@Configuration
class AuthProperties {
    lateinit var tokenSecretKey: String
    lateinit var tokenExpireInMs: String
    lateinit var refreshTokenExpireInMs: String
    lateinit var authorizedRedirectUris: List<String>
    lateinit var corsAllowedOrigins: List<String>
}
