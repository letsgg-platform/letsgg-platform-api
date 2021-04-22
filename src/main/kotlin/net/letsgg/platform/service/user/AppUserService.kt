package net.letsgg.platform.service.user

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.webapi.dto.AppUserRequestDto
import java.util.*

interface AppUserService {

    fun save(user: LetsggUser): LetsggUser
    fun saveAll(users: Iterable<LetsggUser>): List<LetsggUser>
    fun delete(id: UUID)
    fun get(id: UUID): LetsggUser
    fun getByUsername(username: String): LetsggUser
    fun getAll(): List<LetsggUser>
    fun update(updatedUser: AppUserRequestDto, id: UUID): LetsggUser
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(userEmail: String): Boolean
    fun getByEmail(email: String): LetsggUser
    fun getByResetToken(resetToken: String): LetsggUser
}