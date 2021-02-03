package net.letsgg.platform.webapi.controller

import net.letsgg.platform.utility.LoggerDelegate
import net.letsgg.platform.webapi.dto.LoginRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
class UserAuthController(
) {
    private val logger by LoggerDelegate()

    @PostMapping
    fun authenticate(@RequestBody authBody: LoginRequest) {}
//
//    @PostMapping("/sign_up")
//    fun signUp(@RequestBody signUpBody: LetsggUserSignupRequestDto): ResponseEntity<LetsggUserResponseDto> {
//    }
}