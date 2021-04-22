package net.letsgg.platform.service.user

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.exception.InvalidResetPasswordTokenException
import net.letsgg.platform.exception.ResourceNotFoundException
import net.letsgg.platform.repository.LetsggUserRepository
import net.letsgg.platform.repository.PasswordResetTokenRepository
import net.letsgg.platform.utility.LoggerDelegate
import net.letsgg.platform.webapi.dto.AppUserRequestDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CoreAppUserService(
    private val userRepository: LetsggUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
) : AppUserService {
    private val logger by LoggerDelegate()

    @Transactional(readOnly = true)
    override fun get(id: UUID): LetsggUser {
        logger.info("getting user w/ id: {}", id)
        return userRepository.findById(id).orElseThrow {
            ResourceNotFoundException.userIdSupplier(id)
        }
    }

    @Transactional(readOnly = true)
    override fun getByEmail(email: String): LetsggUser {
        logger.info("getting user w/ email: $email")
        return userRepository.findByEmail(email).orElseThrow {
            ResourceNotFoundException.userEmailSupplier(email)
        }
    }

    @Transactional(readOnly = true)
    override fun getByResetToken(resetToken: String): LetsggUser {
        return passwordResetTokenRepository.findByToken(resetToken)
            .orElseThrow { throw InvalidResetPasswordTokenException() }
            .user
    }

    @Transactional(readOnly = true)
    override fun getByUsername(username: String): LetsggUser {
        logger.info("getting user w/ username: $username")
        return userRepository.findByUsername(username).orElseThrow {
            ResourceNotFoundException.userUsernameSupplier(username)
        }
    }

    @Transactional(readOnly = true)
    override fun getAll(): List<LetsggUser> = userRepository.findAll()

    override fun save(user: LetsggUser) = userRepository.saveAndFlush(user)

    override fun saveAll(users: Iterable<LetsggUser>): List<LetsggUser> {
        return userRepository.saveAll(users)
    }

    override fun delete(id: UUID) {
        logger.info("deleting user w/ id: {}", id)
        userRepository.deleteById(id)
    }

    override fun update(updatedUser: AppUserRequestDto, id: UUID): LetsggUser {
        logger.info("updating user w/ id: {}", id)
        val user2Upd = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException.userIdSupplier(id)
        }
        return save(updUserFields(updatedUser, user2Upd))
    }

    private fun updUserFields(updatedUser: AppUserRequestDto, user2Upd: LetsggUser) = user2Upd.apply {
        name = updatedUser.fullName
        email = updatedUser.email
        country = updatedUser.country
        passwordHash = passwordEncoder.encode(updatedUser.password)
    }

    override fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    override fun existsByEmail(userEmail: String): Boolean {
        return userRepository.existsByEmail(userEmail)
    }
}