package net.letsgg.platform.service.user

import net.letsgg.platform.entity.PasswordResetToken
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.exception.EmailAlreadyInUseException
import net.letsgg.platform.exception.InvalidLoginCredentialsException
import net.letsgg.platform.mapper.LetsggUserMapper
import net.letsgg.platform.repository.PasswordResetTokenRepository
import net.letsgg.platform.security.jwt.JwtTokenProviderProxy
import net.letsgg.platform.service.email.EmailSenderService
import net.letsgg.platform.utility.LoggerDelegate
import net.letsgg.platform.webapi.dto.LoginRequest
import net.letsgg.platform.webapi.dto.OauthTokenInfoModel
import net.letsgg.platform.webapi.dto.SignUpRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*


@Service
@Transactional
class AppUserAuthService(
    private val authenticationManager: AuthenticationManager,
    private val userService: AppUserService,
    private val userMapper: LetsggUserMapper,
    private val jwtTokenProviderProxy: JwtTokenProviderProxy,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailSenderService: EmailSenderService,
    private val templateEngine: TemplateEngine
) {
    private val logger by LoggerDelegate()

    fun login(loginRequest: LoginRequest): OauthTokenInfoModel {
        val authentication: Authentication = attemptAuthentication(loginRequest)
        return jwtTokenProviderProxy.createToken(authentication)
    }

    fun register(signUpRequest: SignUpRequest): OauthTokenInfoModel {
        if (userService.existsByEmail(signUpRequest.email)) {
            logger.error("cannot register - this email is associated with another user")
            throw EmailAlreadyInUseException.emailAlreadyInUseSupplier(signUpRequest.email)
        }
        userService.save(userMapper.toEntity(signUpRequest))

        val authentication = attemptAuthentication(
            LoginRequest(signUpRequest.email, signUpRequest.password)
        )
        return jwtTokenProviderProxy.createToken(authentication)
    }

    @Throws(InvalidLoginCredentialsException::class)
    private fun attemptAuthentication(loginRequest: LoginRequest): Authentication {
        try {
            val authentication = UsernamePasswordAuthenticationToken(
                loginRequest.email, loginRequest.password
            )
            return authenticationManager.authenticate(authentication)
        } catch (ex: AuthenticationException) {
            logger.error("Invalid authentication credentials")
            throw InvalidLoginCredentialsException("The sign-in email or password is not correct", ex)
        }
    }

    fun refreshToken(authentication: Authentication): OauthTokenInfoModel {
        return jwtTokenProviderProxy.createToken(authentication)
    }

    fun resetPassword(email: String) {
        val user = userService.getByEmail(email)
        val resetPasswordToken = generateResetPasswordToken()
        createPasswordResetTokenForUser(resetPasswordToken, user)
        sendResetPasswordEmail(user, resetPasswordToken)
    }

    private fun sendResetPasswordEmail(user: LetsggUser, resetPasswordToken: String) {
        val thymeLeafContext = Context().apply {
            setVariable("username", user.username)
            setVariable("resetToken", resetPasswordToken)
        }
        val content = templateEngine.process("reset-password-confirm.html", thymeLeafContext)
        emailSenderService.sendEmailWithThymeleafTemplate(
            content, thymeLeafContext, "Confirm Password Reset",
            user.email, true, listOf()
        )
    }

    fun createPasswordResetTokenForUser(resetToken: String, user: LetsggUser) {
        logger.info("creating password reset token for user ${user.id}")
        val resetPasswordToken = PasswordResetToken(resetToken, user)
        passwordResetTokenRepository.saveAndFlush(resetPasswordToken).token
    }

    private fun generateResetPasswordToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    fun changeUserPassword(newPassword: String, resetToken: String) {
        val user = userService.getByResetToken(resetToken)
        val encodedNewPassword = passwordEncoder.encode(newPassword)
        user.passwordHash = encodedNewPassword
        userService.save(user)
        passwordResetTokenRepository.deleteByUser(user)
    }
}