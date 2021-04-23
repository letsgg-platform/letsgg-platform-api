package net.letsgg.platform.service

import net.letsgg.platform.entity.EmailEntry
import net.letsgg.platform.exception.EmailAlreadyInUseException
import net.letsgg.platform.repository.EmailEntryRepo
import org.springframework.stereotype.Service

@Service
class NewsletterSignUpService(
    private val emailEntryRepo: EmailEntryRepo
) {

    fun signUpForNewsFeed(userEmail: String): EmailEntry {
        val emailEntry = EmailEntry(userEmail)
        if (emailEntryRepo.existsByUserEmail(userEmail)) {
            EmailAlreadyInUseException.alreadySubscribedToNewsFeedSupplier(userEmail)
        }
        return emailEntryRepo.save(emailEntry)
    }

    fun unsubscribeFromNewsFeed(id: String) {
        emailEntryRepo.deleteById(id)
    }
}