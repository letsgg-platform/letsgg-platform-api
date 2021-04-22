package net.letsgg.platform.repository

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.entity.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, UUID> {
    
    fun findByToken(token: String): Optional<PasswordResetToken>
    fun deleteByUser(user: LetsggUser)
}