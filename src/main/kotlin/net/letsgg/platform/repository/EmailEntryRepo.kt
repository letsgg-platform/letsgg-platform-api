package net.letsgg.platform.repository

import net.letsgg.platform.entity.EmailEntry
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EmailEntryRepo : MongoRepository<EmailEntry, String> {

    fun findByUserEmail(userEmail: String): Optional<EmailEntry>

    fun deleteByUserEmail(userEmail: String): Int

    fun existsByUserEmail(userEmail: String): Boolean
}