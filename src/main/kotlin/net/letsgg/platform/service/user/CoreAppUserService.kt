package net.letsgg.platform.service.user

import net.letsgg.platform.api.dto.AppUserRequestDto
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.exception.InvalidResetPasswordTokenException
import net.letsgg.platform.exception.ResourceNotFoundException
import net.letsgg.platform.repository.LetsggUserRepository
import net.letsgg.platform.repository.PasswordResetTokenRepository
import net.letsgg.platform.utility.LoggerDelegate
import net.letsgg.platform.utility.getLocalizedMessage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CoreAppUserService(
  private val userRepository: LetsggUserRepository,
  private val passwordResetTokenRepository: PasswordResetTokenRepository,
) : AppUserService {

  private val logger by LoggerDelegate()

  companion object {
    private val INVALID_RESET_PASSWORD_TOKEN = getLocalizedMessage("reset.password.token.exception")
    private val ENTITY_NOT_EXISTS = getLocalizedMessage("entity.not-exists.exception")
  }

  @Transactional(readOnly = true)
  override fun get(id: UUID): LetsggUser {
    logger.debug("getting user w/ id: {}", id)
    return userRepository.findById(id).orElseThrow {
      logger.error(String.format(ENTITY_NOT_EXISTS, "id", id.toString()))
      ResourceNotFoundException.notExistsByFieldSupplier("id", id.toString())
    }
  }

  @Transactional(readOnly = true)
  override fun getByEmail(email: String): LetsggUser {
    logger.debug("getting user w/ email: $email")
    return userRepository.findByEmail(email).orElseThrow {
      logger.error(String.format(ENTITY_NOT_EXISTS, "email", email))
      ResourceNotFoundException.notExistsByFieldSupplier("email", email)
    }
  }

  @Transactional(readOnly = true)
  override fun getByResetToken(resetToken: String): LetsggUser {
    return passwordResetTokenRepository.findByToken(resetToken)
      .orElseThrow {
        logger.error(String.format(INVALID_RESET_PASSWORD_TOKEN, resetToken))
        throw InvalidResetPasswordTokenException(String.format(INVALID_RESET_PASSWORD_TOKEN, resetToken))
      }
      .user
  }

  @Transactional(readOnly = true)
  override fun getByUsername(username: String): LetsggUser {
    logger.debug("getting user w/ username: $username")
    return userRepository.findByUsername(username).orElseThrow {
      logger.error(String.format(ENTITY_NOT_EXISTS, "username", username))
      ResourceNotFoundException.notExistsByFieldSupplier("username", username)
    }
  }

  @Transactional(readOnly = true)
  override fun getAll(): List<LetsggUser> = userRepository.findAll()

  override fun save(user: LetsggUser) = userRepository.saveAndFlush(user)

  override fun saveAll(users: Iterable<LetsggUser>): List<LetsggUser> {
    return userRepository.saveAll(users)
  }

  override fun delete(id: UUID) {
    logger.debug("deleting user w/ id: {}", id)
    userRepository.deleteById(id)
  }

  override fun update(updatedUser: AppUserRequestDto, id: UUID): LetsggUser {
    logger.debug("updating user w/ id: {}", id)
    val user2Upd = userRepository.findById(id).orElseThrow {
      logger.error(String.format(ENTITY_NOT_EXISTS, "id", id.toString()))
      ResourceNotFoundException.notExistsByFieldSupplier("id", id.toString())
    }
    return save(updUserFields(updatedUser, user2Upd))
  }

  private fun updUserFields(updatedUser: AppUserRequestDto, user2Upd: LetsggUser) = user2Upd.apply {
    name = updatedUser.fullName
    email = updatedUser.email
  }

  @Transactional(readOnly = true)
  override fun existsByUsername(username: String): Boolean {
    return userRepository.existsByUsername(username)
  }

  @Transactional(readOnly = true)
  override fun existsByEmail(userEmail: String): Boolean {
    return userRepository.existsByEmail(userEmail)
  }
}