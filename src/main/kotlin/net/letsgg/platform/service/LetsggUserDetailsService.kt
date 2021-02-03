package net.letsgg.platform.service

import net.letsgg.platform.security.LetsggUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LetsggUserDetailsService(
    private val userService: LetsggUserService,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.getByUsername(username)
        return LetsggUserDetails(user)
    }
}