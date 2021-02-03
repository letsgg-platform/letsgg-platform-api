package net.letsgg.platform.security

import net.letsgg.platform.entity.LetsggUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class LetsggUserDetails(var user: LetsggUser) :
    UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return user.userRole.getGrantedAuthorities()
    }

    override fun getPassword(): String {
        return user.passwordHash
    }

    override fun getUsername(): String {
        return user.username
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
