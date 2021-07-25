package net.letsgg.platform.service.user

import net.letsgg.platform.api.dto.UserDto
import net.letsgg.platform.api.mapper.LetsggUserMapper
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
class CoreUserService(
  private val userRepository: LetsggUserRepository,
  private val passwordResetTokenRepository: PasswordResetTokenRepository,
  private val userMapper: LetsggUserMapper
) : UserService {

  private val logger by LoggerDelegate()

  companion object {
    private val INVALID_RESET_PASSWORD_TOKEN = getLocalizedMessage("reset.password.token.exception")
    private val ENTITY_NOT_EXISTS = getLocalizedMessage("entity.not-exists.exception")
  }

  override fun get(id: UUID): LetsggUser {
    logger.debug("getting user w/ id: {}", id)
    return userRepository.findById(id).orElseThrow {
      logger.error(ENTITY_NOT_EXISTS.format("id", id.toString()))
      ResourceNotFoundException.notExistsByFieldSupplier("id", id.toString())
    }
  }

  override fun getByEmail(email: String): LetsggUser {
    logger.debug("getting user w/ email: $email")
    return userRepository.findByEmail(email).orElseThrow {
      logger.error(ENTITY_NOT_EXISTS.format("email", email))
      ResourceNotFoundException.notExistsByFieldSupplier("email", email)
    }
  }

  override fun getByResetToken(resetToken: String): LetsggUser {
    return passwordResetTokenRepository.findByToken(resetToken)
      .orElseThrow {
        logger.error(INVALID_RESET_PASSWORD_TOKEN.format(resetToken))
        throw InvalidResetPasswordTokenException(String.format(INVALID_RESET_PASSWORD_TOKEN, resetToken))
      }
      .user
  }

  override fun getByUsername(username: String): LetsggUser {
    logger.debug("getting user w/ username: $username")
    return userRepository.findByUsername(username).orElseThrow {
      logger.error(ENTITY_NOT_EXISTS.format("username", username))
      ResourceNotFoundException.notExistsByFieldSupplier("username", username)
    }
  }

  override fun getAll(): List<LetsggUser> = userRepository.findAll()

  @Transactional
  override fun save(user: LetsggUser) = userRepository.saveAndFlush(user)

  @Transactional
  override fun saveAll(users: Iterable<LetsggUser>): List<LetsggUser> {
    return userRepository.saveAll(users)
  }

  @Transactional
  override fun delete(id: UUID) {
    logger.debug("deleting user w/ id: {}", id)
    userRepository.deleteById(id)
  }

  @Transactional
  override fun update(updatedUser: UserDto, id: UUID): LetsggUser {
    logger.debug("updating user w/ id: {}", id)
    val userToUpdate = userRepository.findById(id).orElseThrow {
      logger.error(ENTITY_NOT_EXISTS.format("id", id.toString()))
      ResourceNotFoundException.notExistsByFieldSupplier("id", id.toString())
    }
    return save(userMapper.update(userMapper.convert(updatedUser), userToUpdate))
  }

  override fun existsByUsername(username: String): Boolean {
    return userRepository.existsByUsername(username)
  }

  override fun existsByEmail(userEmail: String): Boolean {
    return userRepository.existsByEmail(userEmail)
  }
}