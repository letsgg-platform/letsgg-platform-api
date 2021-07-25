package net.letsgg.platform.repository

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.entity.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, UUID> {
    fun findByToken(token: String): Optional<PasswordResetToken>
    fun deleteByUser(user: LetsggUser)
}
