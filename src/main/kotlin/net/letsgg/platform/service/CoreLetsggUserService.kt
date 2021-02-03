package net.letsgg.platform.service

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.exception.ResourceNotFoundException
import net.letsgg.platform.repository.LetsggUserRepository
import net.letsgg.platform.utility.LoggerDelegate
import net.letsgg.platform.webapi.dto.LetsggUserRequestDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service("coreLetsggUserService")
@Transactional
class CoreLetsggUserService(
    private val userRepository: LetsggUserRepository,
    private val passwordEncoder: PasswordEncoder,
) : LetsggUserService {
    private val logger by LoggerDelegate()

    override fun save(user: LetsggUser) = userRepository.saveAndFlush(user)
    override fun saveAll(users: Iterable<LetsggUser>): MutableList<LetsggUser> {
        return userRepository.saveAll(users)
    }

    override fun delete(id: UUID) {
        logger.info("deleting user w/ id: {}", id)
        userRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun get(id: UUID): LetsggUser {
        logger.info("getting user w/ id: {}", id)
        return userRepository.findById(id).orElseThrow {
            ResourceNotFoundException.userIdSupplier(id)
        }
    }

    @Transactional(readOnly = true)
    override fun getAll(): List<LetsggUser> = userRepository.findAll()

    @Transactional(readOnly = true)
    override fun getByUsername(username: String): LetsggUser {
        return userRepository.findByUsername(username).orElseThrow {
            ResourceNotFoundException.userUsernameOrEmailSupplier(username)
        }
    }

    override fun update(updatedUser: LetsggUserRequestDto, id: UUID): LetsggUser {
        logger.info("updating user w/ id: {}", id)
        val user2Upd = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException.userIdSupplier(id)
        }
        return save(updUserFields(updatedUser, user2Upd))
    }

    private fun updUserFields(updatedUser: LetsggUserRequestDto, user2Upd: LetsggUser) = user2Upd.apply {
        firstName = updatedUser.firstName
        lastName = updatedUser.lastName
        email = updatedUser.email
        country = updatedUser.country
        passwordHash = passwordEncoder.encode(updatedUser.password)
    }
}