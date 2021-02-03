package net.letsgg.platform.security

object Preauthorized {
    const val WITH_ROLE_ADMIN = "hasRole('ADMIN')"
    const val WITH_ROLE_USER = "hasRole('USER')"
    const val WITH_AUTHORITY_READ = "hasAuthority('read')"
    const val WITH_AUTHORITY_MODIFY = "hasAuthority('modify')"
}