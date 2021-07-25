package net.letsgg.platform.security

import net.letsgg.platform.entity.LetsggUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class AppUserDetails(val user: LetsggUser) :
    UserDetails, OAuth2User {

    private var attributes: Map<String, Any>? = null

    constructor(user: LetsggUser, attributes: Map<String, Any>) : this(user) {
        this.attributes = attributes
    }

    override fun getName(): String {
        return user.email
    }

    override fun getAttributes(): Map<String, Any>? {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return user.userRole.getGrantedAuthorities()
    }

    override fun getPassword(): String {
        return user.passwordHash
    }

    override fun getUsername(): String {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
