package net.letsgg.platform.webapi.controller

import net.letsgg.platform.mapper.LetsggUserMapper
import net.letsgg.platform.security.Preauthorized
import net.letsgg.platform.security.oauth2.OauthTokenInfo
import net.letsgg.platform.service.AppTokenService
import net.letsgg.platform.service.user.AppUserAuthService
import net.letsgg.platform.utility.LoggerDelegate
import net.letsgg.platform.webapi.dto.LoginRequest
import net.letsgg.platform.webapi.dto.OauthTokenInfoModel
import net.letsgg.platform.webapi.dto.SignUpRequest
import net.letsgg.platform.webapi.dto.UserAuthResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("api/auth")
@Validated
class UserAuthController(
    private val userAuthService: AppUserAuthService,
    private val userMapper: LetsggUserMapper,
    private val tokenService: AppTokenService
) {
    private val logger by LoggerDelegate()

//    @CrossOrigin(allowCredentials = )
    @PostMapping("/login")
    fun loginUser(
        @RequestBody @Valid loginRequest: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        val authResponseBody = userAuthService.login(loginRequest, response)
        val cookie = ResponseCookie.from("ussajwt", authResponseBody.accessToken)
            .domain("letsgg.net")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(authResponseBody.expiresIn)
            .build()
        return ResponseEntity
            .ok()
//            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, true.toString())
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build()
    }

    @PostMapping("/register")
    fun signUp(
        @RequestBody @Valid signUpRequest: SignUpRequest,
        response: HttpServletResponse
    ): ResponseEntity<OauthTokenInfoModel> {
        val responseBody = userAuthService.register(signUpRequest, response)
        return ResponseEntity(responseBody, HttpStatus.CREATED)
    }

    @PostMapping("/reset-password-request")
    fun resetPassword(@RequestParam email: String): ResponseEntity<Unit> {
        userAuthService.resetPassword(email)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    @PostMapping("/refresh-token")
    @PreAuthorize("isAuthenticated()")
    fun refreshToken(authentication: Authentication): ResponseEntity<OauthTokenInfoModel> { //FIXME, read refresh_token, not security context.
        return ResponseEntity(userAuthService.refreshToken(authentication), HttpStatus.OK)
    }

    @GetMapping("/reset-password-confirm")
    fun showChangePasswordPage(@RequestParam token: String) {
        tokenService.validateResetPasswordToken(token)
        //if validation ok, render and show page
    }

    @PostMapping("/update-password")
    fun updatePassword(@RequestBody changePasswordDto: ChangePasswordDto): ResponseEntity<Unit> {
        tokenService.validateResetPasswordToken(changePasswordDto.token)

        userAuthService.changeUserPassword(changePasswordDto.newPassword, changePasswordDto.token)
        return ResponseEntity(HttpStatus.OK)
    }

    data class ChangePasswordDto(
        val token: String, val newPassword: String,
    )

    @RestController
    @RequestMapping("api/oauth2")
    internal class UserOauth2Controller(
        private val tokenService: AppTokenService,
    ) {


        @PostMapping("/token")
        fun getAccessToken(
            @RequestParam("code") authorizationCode: String,
            request: HttpServletRequest
        ): ResponseEntity<OauthTokenInfoModel> {
            val oauthTokenInfo =
                tokenService.getOauthTokenByAuthorizationCode(authorizationCode, request)
            return ResponseEntity(oauthTokenInfo, HttpStatus.OK)
        }
    }
}