package net.letsgg.platform.api.resource.population

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.entity.type.AuthProvider
import net.letsgg.platform.entity.type.Gender
import net.letsgg.platform.security.AppUserRole
import net.letsgg.platform.service.user.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/population-resource")
class PopulationResource(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) {

    @PostMapping
    fun populateUsers() {
        val user1 = LetsggUser(
            "Roman Samurai", "romm1", "romantupss@ukr.net",
            Gender.UNDEFINED, passwordEncoder.encode("foo"), AppUserRole.USER, AuthProvider.LOCAL, "letsgg-platform"
        )
        userService.save(user1)
    }
}
