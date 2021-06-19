package net.letsgg.platform.repository

import net.letsgg.platform.entity.EmailEntry
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmailEntryRepository : JpaRepository<EmailEntry, UUID> {
  fun findByUserEmail(userEmail: String): Optional<EmailEntry>
  fun deleteByUserEmail(userEmail: String): Int
  fun existsByUserEmail(userEmail: String): Boolean
}