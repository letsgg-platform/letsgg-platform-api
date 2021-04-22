package net.letsgg.platform.repository

import net.letsgg.platform.entity.EmailEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmailEntryRepo : JpaRepository<EmailEntry, UUID> {

    fun findByUserEmail(userEmail: String): Optional<EmailEntry>

    fun deleteByUserEmail(userEmail: String): Int

    fun existsByUserEmail(userEmail: String): Boolean
}