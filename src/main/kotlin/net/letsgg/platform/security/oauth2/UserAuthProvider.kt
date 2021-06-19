package net.letsgg.platform.security.oauth2

enum class UserAuthProvider(val providerId: String) {
  LOCAL("letsgg-platform"),
  GOOGLE(""),
  GITHUB("")
}