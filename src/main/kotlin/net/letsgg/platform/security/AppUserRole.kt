package net.letsgg.platform.security

import net.letsgg.platform.security.AppUserPermission.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

enum class AppUserRole(private val permissions: Set<AppUserPermission>) {
    ADMIN(setOf(READ, MODIFY, USER_INFO)),
    USER(setOf(READ, USER_INFO)),
    UNFINISHED_SETUP_USER(setOf(USER_INFO));

    fun getGrantedAuthorities(): Set<SimpleGrantedAuthority> {
        val permissions: MutableSet<SimpleGrantedAuthority> = permissions.stream()
            .map { permission -> SimpleGrantedAuthority(permission.getPermission()) }
            .collect(Collectors.toSet())
        permissions.add(SimpleGrantedAuthority("ROLE_$name"))
        return permissions
    }

    override fun toString(): String {
        return "ROLE_${this.name}"
    }
}
