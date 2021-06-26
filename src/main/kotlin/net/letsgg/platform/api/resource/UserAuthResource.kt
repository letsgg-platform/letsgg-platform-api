package net.letsgg.platform.api.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import net.letsgg.platform.api.dto.LoginRequest
import net.letsgg.platform.api.dto.OauthTokenInfoDto
import net.letsgg.platform.api.dto.SignUpRequest
import net.letsgg.platform.api.mapper.LetsggUserMapper
import net.letsgg.platform.api.mapper.OauthTokenInfoMapper
import net.letsgg.platform.exception.handler.ApiError
import net.letsgg.platform.service.auth.CoreUserAuthService
import net.letsgg.platform.service.auth.token.AppTokenService
import net.letsgg.platform.service.oauth.OauthTokenService
import net.letsgg.platform.service.user.AppUserSettingsService
import net.letsgg.platform.utility.CookieUtils
import net.letsgg.platform.utility.LoggerDelegate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("api/auth")
@Validated
class UserAuthResource(
  private val userSettingsService: AppUserSettingsService,
  private val userAuthService: CoreUserAuthService,
  private val userMapper: LetsggUserMapper,
  private val tokenService: AppTokenService
) {
  private val logger by LoggerDelegate()

  @Operation(summary = "User Login")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200", description = "Successful Login",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = OauthTokenInfoDto::class)
          )
        ]
      ),
      ApiResponse(
        responseCode = "401", description = "Invalid Credentials Supplied",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiError::class)
          )
        ]
      )
    ]
  )
  @PostMapping("/login")
  fun loginUser(
    @RequestBody loginRequest: LoginRequest,
    response: HttpServletResponse
  ): ResponseEntity<OauthTokenInfoDto> {
    logger.debug("logging in ${loginRequest.email}")
    val oauthTokenInfo = userAuthService.login(loginRequest)
    CookieUtils.setAuthCookies(oauthTokenInfo, response)
    return ResponseEntity(oauthTokenInfo, HttpStatus.OK)
  }

  @Operation(summary = "User Register")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "201", description = "User Created",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = OauthTokenInfoDto::class)
          )
        ]
      ),
      ApiResponse(
        responseCode = "406", description = "Validation Failed For Supplied Fields",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiError::class)
          )
        ]
      )
    ]
  )
  @PostMapping("/register")
  fun signUp(
    @RequestBody @Valid signUpRequest: SignUpRequest,
    response: HttpServletResponse
  ): ResponseEntity<OauthTokenInfoDto> {
    logger.debug("signing up user w/ email ${signUpRequest.email}")
    val oauthTokenInfo = userAuthService.register(signUpRequest)
    CookieUtils.setAuthCookies(oauthTokenInfo, response)
    return ResponseEntity(oauthTokenInfo, HttpStatus.CREATED)
  }

  @PostMapping("/reset-password-request")
  fun resetPassword(@RequestParam email: String): ResponseEntity<Unit> {
    userSettingsService.resetPassword(email)
    return ResponseEntity(Unit, HttpStatus.OK)
  }

  @PostMapping("/refresh-token")
//    @PreAuthorize("isAuthenticated()")
  fun refreshToken(authentication: Authentication): ResponseEntity<OauthTokenInfoDto> { //FIXME, read refresh_token, not security context.
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

    userSettingsService.changeUserPassword(changePasswordDto.newPassword, changePasswordDto.token)
    return ResponseEntity(HttpStatus.OK)
  }

  data class ChangePasswordDto(
    val token: String, val newPassword: String,
  )

  @RestController
  @RequestMapping("api/oauth2")
  internal class UserOauth2Controller(
    private val oauthTokenService: OauthTokenService,
    private val oauthTokenInfoMapper: OauthTokenInfoMapper
  ) {

    private val logger by LoggerDelegate()

    @PostMapping("/token")
    fun getAccessToken(
      @RequestParam("code") authorizationCode: String,
      request: HttpServletRequest,
      response: HttpServletResponse
    ): ResponseEntity<OauthTokenInfoDto> {
      logger.debug("getting oauth access token with code {}", authorizationCode)
      val oauthTokenInfo =
        oauthTokenService.getTokenByAuthorizationCode(authorizationCode, request)
      oauthTokenInfo.id?.let {
        oauthTokenService.deleteById(it)
      }
      val oauthTokenInfoDto = oauthTokenInfoMapper.toDto(oauthTokenInfo)
      CookieUtils.setAuthCookies(oauthTokenInfoDto, response)
      return ResponseEntity(oauthTokenInfoDto, HttpStatus.OK)
    }
  }
}