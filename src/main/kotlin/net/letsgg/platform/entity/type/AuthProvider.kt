package net.letsgg.platform.entity.type

enum class AuthProvider(val providerId: String) {
    LOCAL("letsgg-platform"),
    GOOGLE(""),
    GITHUB(""),
    FACEBOOK("")
}