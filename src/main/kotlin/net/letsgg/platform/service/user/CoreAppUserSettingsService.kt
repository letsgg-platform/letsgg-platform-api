package net.letsgg.platform.service.user

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.entity.PasswordResetToken
import net.letsgg.platform.repository.PasswordResetTokenRepository
import net.letsgg.platform.service.email.EmailSenderService
import net.letsgg.platform.utility.LoggerDelegate
import net.letsgg.platform.utility.PasswordResetTokenUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Component
@Transactional
class CoreAppUserSettingsService(
  private val passwordResetTokenRepository: PasswordResetTokenRepository,
  private val userService: AppUserService,
  private val passwordEncoder: PasswordEncoder,
  private val emailSenderService: EmailSenderService,
  private val templateEngine: TemplateEngine
) : AppUserSettingsService {

  private val log by LoggerDelegate()

  override fun createPasswordResetTokenForUser(resetToken: String, user: LetsggUser) {
    log.info("creating password reset token for user ${user.id}")
    val resetPasswordToken = PasswordResetToken(resetToken, user)
    passwordResetTokenRepository.saveAndFlush(resetPasswordToken).token
  }

  override fun changeUserPassword(newPassword: String, resetToken: String) {
    val user = userService.getByResetToken(resetToken)
    val encodedNewPassword = passwordEncoder.encode(newPassword)
    user.passwordHash = encodedNewPassword
    userService.save(user)
    passwordResetTokenRepository.deleteByUser(user)
  }

  override fun resetPassword(email: String) {
    val user = userService.getByEmail(email)
    val resetPasswordToken = PasswordResetTokenUtils.generateResetPasswordToken()
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
}