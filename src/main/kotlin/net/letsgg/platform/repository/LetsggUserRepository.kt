package net.letsgg.platform.repository

import net.letsgg.platform.entity.LetsggUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LetsggUserRepository : JpaRepository<LetsggUser, UUID> {
    fun findByUsername(username: String): Optional<LetsggUser>
    fun findByEmail(email: String): Optional<LetsggUser>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}