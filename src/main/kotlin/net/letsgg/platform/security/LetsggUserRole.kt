package net.letsgg.platform.security

import net.letsgg.platform.security.LetsggUserPermission.MODIFY
import net.letsgg.platform.security.LetsggUserPermission.READ
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

enum class LetsggUserRole(private val permissions: Set<LetsggUserPermission>) {
    ADMIN(setOf(READ, MODIFY)),
    USER(setOf(READ));

    fun getGrantedAuthorities(): Set<SimpleGrantedAuthority> {
        val permissions: MutableSet<SimpleGrantedAuthority> = permissions.stream()
            .map { permission -> SimpleGrantedAuthority(permission.getPermission()) }
            .collect(Collectors.toSet())
        permissions.add(SimpleGrantedAuthority("ROLE_$name"))
        return permissions
    }
}