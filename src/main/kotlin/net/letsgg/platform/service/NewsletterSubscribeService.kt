package net.letsgg.platform.service

import net.letsgg.platform.entity.EmailEntry
import net.letsgg.platform.exception.EmailAlreadyInUseException
import net.letsgg.platform.repository.EmailEntryRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.Email


@Service
@Transactional
@Validated
class NewsletterSubscribeService(
    private val emailEntryRepo: EmailEntryRepo
) {

    fun signUpForNewsFeed(@Email userEmail: String): EmailEntry {
        val emailEntry = EmailEntry(userEmail)
        if (emailEntryRepo.existsByUserEmail(userEmail)) {
            EmailAlreadyInUseException.alreadySubscribedToNewsFeedSupplier(userEmail)
        }
        return emailEntryRepo.save(emailEntry)
    }

    fun unsubscribeFromNewsFeed(id: UUID) {
        emailEntryRepo.deleteById(id)
    }
}