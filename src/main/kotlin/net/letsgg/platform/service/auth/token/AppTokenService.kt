package net.letsgg.platform.service.auth.token

import net.letsgg.platform.exception.InvalidResetPasswordTokenException
import net.letsgg.platform.repository.PasswordResetTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class AppTokenService(
  private val resetTokenRepository: PasswordResetTokenRepository,
) {

  fun validateResetPasswordToken(token: String) {
    val persistedToken =
      resetTokenRepository.findByToken(token).orElseThrow { throw InvalidResetPasswordTokenException() }

    if (persistedToken.expireDate.isBefore(Instant.now())) {
      throw InvalidResetPasswordTokenException("token expired")
    }
  }
}