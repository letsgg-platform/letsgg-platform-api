package net.letsgg.platform.security

enum class LetsggUserPermission(private val permission: String) {
    MODIFY("modify"),
    READ("read");

    fun getPermission(): String = this.permission
}