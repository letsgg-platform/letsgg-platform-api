package net.letsgg.platform.security

import net.letsgg.platform.service.user.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AppUserDetailsService(
    private val userService: UserService,
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userService.getByEmail(email)
        return AppUserDetails(user)
    }
}
