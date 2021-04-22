package net.letsgg.platform.security

enum class AppUserPermission(private val permission: String) {
    MODIFY("modify"),
    READ("read"),
    USER_INFO("user_info");

    fun getPermission(): String = this.permission
}