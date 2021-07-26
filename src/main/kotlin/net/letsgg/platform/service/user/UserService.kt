package net.letsgg.platform.service.user

import net.letsgg.platform.api.dto.UserDto
import net.letsgg.platform.entity.LetsggUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface UserService {
    fun save(user: LetsggUser): LetsggUser
    fun saveAll(users: Iterable<LetsggUser>): List<LetsggUser>
    fun delete(id: UUID)
    fun get(id: UUID): LetsggUser
    fun getByUsername(username: String): LetsggUser
    fun getAll(pageable: Pageable): Page<LetsggUser>
    fun update(updatedUser: UserDto, email: String): LetsggUser
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(userEmail: String): Boolean
    fun getByEmail(email: String): LetsggUser
    fun getByResetToken(resetToken: String): LetsggUser
}