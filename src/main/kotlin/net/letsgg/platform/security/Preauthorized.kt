package net.letsgg.platform.security

object Preauthorized {
    const val WITH_ROLE_ADMIN = "hasRole('ADMIN')"
    const val WITH_ROLE_USER = "hasRole('USER')"
    const val WITH_AUTHORITY_READ = "hasAuthority('read')"
    const val WITH_AUTHORITY_MODIFY = "hasAuthority('modify')"
    const val WITH_AUTHORITY_USER_INFO = "hasAuthority('user_info')"
    const val WITH_ROLE_UNFINISHED_SETUP_USER = "hasRole('UNFINISHED_SETUP_USER')"
}
