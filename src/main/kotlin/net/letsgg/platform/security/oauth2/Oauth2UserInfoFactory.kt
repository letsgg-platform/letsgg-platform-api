package net.letsgg.platform.security.oauth2

import net.letsgg.platform.entity.type.AuthProvider.*
import net.letsgg.platform.exception.OAuth2AuthenticationProcessingException

object Oauth2UserInfoFactory {

    fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return when (registrationId.toUpperCase()) {
            GOOGLE.toString() -> GoogleOAuth2UserInfo(attributes)
            GITHUB.toString() -> GithubOAuth2UserInfo(attributes)
            else -> throw OAuth2AuthenticationProcessingException("Login with $registrationId is not supported yet.")
        }
    }
}
