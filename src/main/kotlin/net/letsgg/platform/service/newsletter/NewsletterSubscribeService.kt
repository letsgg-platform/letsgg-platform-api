package net.letsgg.platform.service.newsletter

import net.letsgg.platform.entity.EmailEntry
import net.letsgg.platform.exception.EmailAlreadyInUseException
import net.letsgg.platform.repository.EmailEntryRepository
import net.letsgg.platform.utility.EMAIL_ALREADY_USED
import net.letsgg.platform.utility.LoggerDelegate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.Email


@Service
@Transactional
@Validated
@Deprecated(message = "Will be replaced with User Mail Preferences")
class NewsletterSubscribeService(
    private val emailEntryRepository: EmailEntryRepository
) {

    private val logger by LoggerDelegate()

    fun signUpForNewsFeed(@Email userEmail: String): EmailEntry {
        val emailEntry = EmailEntry(userEmail)
        if (emailEntryRepository.existsByUserEmail(userEmail)) {
            logger.error(String.format(EMAIL_ALREADY_USED, userEmail))
            throw EmailAlreadyInUseException(String.format(EMAIL_ALREADY_USED, userEmail))
        }
        return emailEntryRepository.save(emailEntry)
    }

    fun unsubscribeFromNewsFeed(id: UUID) {
        emailEntryRepository.deleteById(id)
    }
}