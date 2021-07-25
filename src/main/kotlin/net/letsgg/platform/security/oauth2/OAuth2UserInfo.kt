package net.letsgg.platform.security.oauth2

import java.util.*

sealed class OAuth2UserInfo(
    val attributes: Map<String, Any>
) {
    abstract fun getId(): String
    abstract fun getName(): String
    abstract fun getEmail(): String
    abstract fun getImageUrl(): String
    abstract fun getLogin(): String
}

class GoogleOAuth2UserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return attributes["sub"] as String
    }

    override fun getName(): String {
        return attributes["name"] as String
    }

    override fun getEmail(): String {
        return attributes["email"] as String
    }

    override fun getImageUrl(): String {
        return attributes["picture"] as String
    }

    override fun getLogin(): String {
        return (attributes["email"] as String).substringBeforeLast("@")
    }
}

class GithubOAuth2UserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return (attributes["id"] as Int).toString()
    }

    override fun getName(): String {
        return attributes["name"] as String
    }

    override fun getEmail(): String {
        return attributes["email"] as String
    }

    override fun getImageUrl(): String {
        return attributes["avatar_url"] as String
    }

    override fun getLogin(): String {
        return attributes["login"] as String
    }
}
