package net.letsgg.platform.service

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.webapi.dto.LetsggUserRequestDto
import java.util.*

interface LetsggUserService {

    fun save(user: LetsggUser): LetsggUser
    fun saveAll(users: Iterable<LetsggUser>): MutableList<LetsggUser>
    fun delete(id: UUID)
    fun get(id: UUID): LetsggUser
    fun getByUsername(username: String): LetsggUser
    fun getAll(): List<LetsggUser>
    fun update(updatedUser: LetsggUserRequestDto, id: UUID): LetsggUser
}