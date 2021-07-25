package net.letsgg.platform.service.auth

import net.letsgg.platform.api.dto.LoginRequest
import net.letsgg.platform.api.dto.OauthTokenInfoDto
import net.letsgg.platform.api.dto.UserDto
import net.letsgg.platform.api.mapper.LetsggUserMapper
import net.letsgg.platform.exception.EmailAlreadyInUseException
import net.letsgg.platform.exception.InvalidLoginCredentialsException
import net.letsgg.platform.security.jwt.AuthorizationTokenService
import net.letsgg.platform.service.user.UserService
import net.letsgg.platform.utility.EMAIL_ALREADY_USED
import net.letsgg.platform.utility.INVALID_LOGIN_CREDENTIALS
import net.letsgg.platform.utility.LoggerDelegate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CoreUserAuthService(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val userMapper: LetsggUserMapper,
    private val authorizationTokenService: AuthorizationTokenService,
) : UserAuthService {
    private val logger by LoggerDelegate()

    override fun login(loginRequest: LoginRequest): OauthTokenInfoDto {
        val authentication = try {
            attemptAuthentication(loginRequest)
        } catch (ex: AuthenticationException) {
            logger.error(String.format(INVALID_LOGIN_CREDENTIALS, ex.localizedMessage), ex)
            throw InvalidLoginCredentialsException(INVALID_LOGIN_CREDENTIALS, ex)
        }
        return authorizationTokenService.createToken(authentication)
    }

    override fun register(userDto: UserDto): OauthTokenInfoDto {
        if (userService.existsByEmail(userDto.email)) {
            logger.error(String.format(EMAIL_ALREADY_USED, userDto.email))
            throw EmailAlreadyInUseException(String.format(EMAIL_ALREADY_USED, userDto.email))
        }
        userService.save(userMapper.convert(userDto))

        val authentication = attemptAuthentication(
            LoginRequest(userDto.email, userDto.password!!)
        )
        return authorizationTokenService.createToken(authentication)
    }

    @Throws(AuthenticationException::class)
    private fun attemptAuthentication(loginRequest: LoginRequest): Authentication {
        val authentication = UsernamePasswordAuthenticationToken(
            loginRequest.email, loginRequest.password
        )
        return authenticationManager.authenticate(authentication)
    }

    // FIXME set in cookies instead
    fun refreshToken(authentication: Authentication): OauthTokenInfoDto {
        return authorizationTokenService.createToken(authentication)
    }
}
