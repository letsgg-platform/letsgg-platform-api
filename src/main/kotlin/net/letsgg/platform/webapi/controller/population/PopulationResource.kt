package net.letsgg.platform.webapi.controller.population

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.security.AppUserRole
import net.letsgg.platform.security.oauth2.UserAuthProvider
import net.letsgg.platform.service.user.AppUserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/population-resource")
class PopulationResource(
    private val userService: AppUserService,
    private val passwordEncoder: PasswordEncoder,
) {

    @PostMapping
    fun populateUsers() {
        val user1 = LetsggUser(
            "Roman Samurai", "romm1", "romantupss@ukr.net",
            passwordEncoder.encode("foo"), AppUserRole.USER, UserAuthProvider.LOCAL, "letsgg-platform"
        )
        userService.save(user1)
    }
}