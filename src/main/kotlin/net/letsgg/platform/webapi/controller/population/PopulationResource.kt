package net.letsgg.platform.webapi.controller.population

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.security.Preauthorized
import net.letsgg.platform.service.LetsggUserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("api/population-resource")
class PopulationResource(
    private val userService: LetsggUserService,
    private val passwordEncoder: PasswordEncoder,
) {

    @PostMapping
    @PreAuthorize(Preauthorized.WITH_AUTHORITY_READ)
    fun populateUsers() {
        val user1 = LetsggUser(
            "Roman", "Samurai", "romm1", "romantupss@gmail.com",
            passwordEncoder.encode("foo"), LocalDate.now(), "UA"
        )
        userService.save(user1)
    }
}